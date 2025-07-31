# Personal AI Assistant for Linux

> A conversational AI that greets you at startup, right from your Linux terminal.

This project is a custom-built, terminal-based AI assistant that integrates with the Linux desktop. It's designed to provide a more personal and interactive boot-up experience by delivering a time-sensitive greeting and engaging in a conversation powered by Google's Gemini API.

## Demo
<img width="1834" height="2756" alt="carbon" src="https://github.com/user-attachments/assets/23a1935a-b959-47eb-b7ba-a491f2432b7b" />

## Features

* **Personalized Greetings:** Delivers a "Good Morning," "Good Afternoon," or "Good Evening" message based on the system time.
* **Conversational AI:** Utilizes Google's Gemini API for natural and intelligent conversations.
* **Startup Integration:** Designed to launch automatically when you log into your Linux desktop environment.
* **Terminal Interface:** A clean, lightweight, and responsive interface that runs entirely within the terminal.

## Tech Stack

* **Backend:** Java 24
* **Framework:** Spring Boot 3 & Spring Shell
* **AI Service:** Google Gemini Pro API
* **Build Tool:** Gradle / Maven
* **Scripting:** Bash
* **Platform:** Linux (developed on Linux Mint)

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* **Java JDK 24** or higher.
* **Gradle** or **Maven**.
* A **Google Cloud Account** with:
    * Billing enabled.
    * The "Generative Language API" or "Vertex AI API" enabled.
    * A valid API Key.

### Installation

1.  **Clone the repository:**
    ```sh
    git clone [https://github.com/your-username/your-repo-name.git](https://github.com/your-username/your-repo-name.git)
    cd your-repo-name
    ```

2.  **Configure the API Key:**
    Create an `application.properties` file in the `src/main/resources/` directory and add your credentials:
    ```properties
    # Gemini API Configuration
    gemini.api.key=YOUR_GEMINI_API_KEY_HERE
    gemini.api.endpoint=[https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent](https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent)
    ```

3.  **Build the Project:**
    Open a terminal in the project's root directory and run the build command for your chosen tool.

    * **For Gradle:**
        ```sh
        ./gradlew bootJar
        ```
    * **For Maven:**
        ```sh
        mvn clean package
        ```
    This will create an executable `.jar` file in the `build/libs/` or `target/` directory.

## Usage

The project uses two scripts to run. Ensure you update the paths inside these scripts to match your system.

* `run-app.sh`: Executes the Java application.
* `launcher.sh`: Opens a new terminal and runs the `run-app.sh` script.

1.  **Test the Launcher:**
    To see the application in action, run the launcher script from your terminal:
    ```sh
    ./launcher.sh
    ```
    This should open a new terminal window with the AI chat running.

2.  **Set up as a Startup Application (Linux Mint):**
    * Open the **"Startup Applications"** tool.
    * Click **"Add"** -> **"Custom Command"**.
    * Fill in the details:
        * **Name:** `AI Assistant`
        * **Command:** Provide the **full, absolute path** to your `launcher.sh` script (e.g., `/home/your-user/your-project/launcher.sh`).
        * **Startup delay:** Set to `10` seconds.

    The application will now launch automatically every time you log in.

## License

This project is licensed under the MIT License.
