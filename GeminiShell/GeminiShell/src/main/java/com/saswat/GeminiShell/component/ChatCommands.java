package com.saswat.GeminiShell.component;

import com.saswat.GeminiShell.service.GeminiService;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ShellComponent
public class ChatCommands {

    private final GeminiService chatService;
    private final Terminal terminal;

    public ChatCommands(GeminiService chatService, Terminal terminal) {
        this.chatService = chatService;
        this.terminal = terminal;
    }

    @ShellMethod(key = "start", value = "Starts the AI chat session.")
    public void start(@ShellOption(arity = 10) String[] greeting) {

        String initialGreeting = Stream.of(greeting).collect(Collectors.joining(" "));

        String firstAiResponse = chatService.generateResponse("", initialGreeting);
        printAiResponse(firstAiResponse);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printUserPrompt();
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("exit") || userInput.equalsIgnoreCase("quit")) {
                printSystemMessage("Goodbye, Saswat!");
                break;
            }


            String subsequentAiResponse = chatService.generateResponse(userInput, null);
            printAiResponse(subsequentAiResponse);
        }
    }


    private void printAiResponse(String text) {
        AttributedStringBuilder builder = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.bold().foreground(AttributedStyle.CYAN))
                .append("Gemini: ")
                .style(AttributedStyle.DEFAULT)
                .append(text);
        terminal.writer().println(builder.toAnsi());
        terminal.flush();
    }

    private void printUserPrompt() {
        AttributedStringBuilder builder = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.bold().foreground(AttributedStyle.GREEN))
                .append("Saswat > ");
        terminal.writer().print(builder.toAnsi());
        terminal.flush();
    }

    private void printSystemMessage(String text) {
        AttributedStringBuilder builder = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                .append(text);
        terminal.writer().println(builder.toAnsi());
        terminal.flush();
    }
}