package ru.yandex.practicum.filmorate;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

@SpringBootApplication
public class FilmorateApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder()
				.banner((environment, sourceClass, out) -> out.print("\n" +
						" __   __ _    _   _ ____  _______  __        ____  ____      _    ____ _____ ___ ____ _   _ __  __ \n" +
						" \\ \\ / // \\  | \\ | |  _ \\| ____\\ \\/ /       |  _ \\|  _ \\    / \\  / ___|_   _|_ _/ ___| | | |  \\/  |\n" +
						"  \\ V // _ \\ |  \\| | | | |  _|  \\  /        | |_) | |_) |  / _ \\| |     | |  | | |   | | | | |\\/| |\n" +
						"   | |/ ___ \\| |\\  | |_| | |___ /  \\        |  __/|  _ <  / ___ \\ |___  | |  | | |___| |_| | |  | |\n" +
						"   |_/_/   \\_\\_| \\_|____/|_____/_/\\_\\       |_|   |_| \\_\\/_/   \\_\\____| |_| |___\\____|\\___/|_|  |_|\n" +
						"                                                                                                   \n"))
				.sources(FilmorateApplication.class)
				.run(args);

		//SpringApplication.run(FilmorateApplication.class, args);
	}

}
