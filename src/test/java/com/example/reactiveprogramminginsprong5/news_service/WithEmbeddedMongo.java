package com.example.reactiveprogramminginsprong5.news_service;

import com.example.reactiveprogramminginsprong5.news_service.dto.News;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.connection.*;
import com.mongodb.connection.netty.NettyStreamFactory;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
public interface WithEmbeddedMongo {

    AtomicReference<MongodExecutable> MONGO_HOLDER = new AtomicReference<>();

    @BeforeClass
    static void setUpMongo() throws IOException {
        var starter = MongodStarter.getDefaultInstance();

        var bindIp = "localhost";
        var port = 27017;
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.DEVELOPMENT)
                .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                .build();

        var mongodExecutable = starter.prepare(mongodConfig);
        MONGO_HOLDER.set(mongodExecutable);
        mongodExecutable.start();
    }

    @AfterClass
    static void tearDownMongo() {
        MONGO_HOLDER.get().stop();
    }

    default MongoClient mongoClient() {
        var connectionString = new ConnectionString("mongodb://localhost/news");
        var builder = MongoClientSettings.builder()
                .streamFactoryFactory(NettyStreamFactory::new)
                .applyToClusterSettings(cs -> cs.applyConnectionString())
                .applyToConnectionPoolSettings(cps -> cps.applyConnectionString(connectionString))
                .applyToServerSettings(ss -> ss.applyConnectionString(connectionString))
                .applyToSslSettings(ss -> ss.applyConnectionString(connectionString))
                .applyToSocketSettings(ss -> ss.applyConnectionString(connectionString))
                .codecRegistry(fromRegistries(
                        MongoClients.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder()
                                .automatic(true)
                                .register(News.class)
                                .build())
                ));

        if (connectionString.getReadPreference() != null) {
            builder.readPreference(connectionString.getReadPreference());
        }
        if (connectionString.getReadConcern() != null) {
            builder.writeConcern(connectionString.getWriteConcern());
        }
        if (connectionString.getApplicationName() != null) {
            builder.applicationName(connectionString.getApplicationName());
        }

        return MongoClients.create(builder.build());
    }

}