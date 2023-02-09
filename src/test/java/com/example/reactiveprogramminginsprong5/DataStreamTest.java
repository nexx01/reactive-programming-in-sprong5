package com.example.reactiveprogramminginsprong5;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

import static com.sun.nio.file.ExtendedOpenOption.NOSHARE_WRITE;

@Slf4j
public class DataStreamTest {

    @Test
    void dataBufferUtild() {
        var flux = DataBufferUtils
                .read(
                        new DefaultResourceLoader().getResource("hamlet.txt"),
                        new DefaultDataBufferFactory(),
                        1
                )
                .log()
                ;
//
        var write = DataBufferUtils.write(flux, Path.of("src/test/resources/hamletCopy.txt"))
                .log();

        var subscribe = write.subscribe();

        while (!subscribe.isDisposed()) {

        }

    }

}
