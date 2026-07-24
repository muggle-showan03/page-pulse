# AI Usage Statement

For this internship submission task, AI tools were used responsibly as a collaborative assistant, pair programmer, and learning aid to accelerate the development of the Page Pulse application.

## 1. AI Tools Used
- **Anthropic Claude 3.5 Sonnet / OpenAI GPT-4o:** Used via IDE integration for code generation, refactoring, and architectural discussions.
- **GitHub Copilot (if applicable):** Used for inline code completions and writing repetitive test boilerplate.

## 2. How the AI was Used
- **Bootstrapping:** Scaffolding the React frontend and setting up the initial Vite configuration.
- **Styling:** Generating the foundational CSS variables and glassmorphism design system to ensure a modern, responsive UI.
- **Regex & Utilities:** Assisting in writing the regular expression for the `UrlValidator` to accurately parse `http/https` schemas.
- **Test Generation:** Helping generate the boilerplate for JUnit 5 and Mockito tests, especially the setup code using `ReflectionTestUtils`.
- **Debugging:** Explaining Spring Boot 4.1.0 test annotation package changes (e.g., the move of `@WebMvcTest`).

## 3. Manual Implementation & Review Process
Although AI was used to accelerate development, all generated code was strictly reviewed, understood, and often heavily modified before integration:
- **Architecture Decisions:** The decision to keep the controller thin and move all validation and HTTP fetching logic into `AnalyzeService` was a manual architectural choice.
- **Code Audits:** I reviewed every line of the generated CSS and React components to ensure the UI met the specific design aesthetic ("wow factor") requested in the prompt.
- **Error Handling:** The `GlobalExceptionHandler` was manually orchestrated to ensure precise HTTP status codes (e.g., returning 408 for Request Timeout and 502 for Bad Gateway).
- **Security & Quality:** I manually verified that `Jsoup` was safely parsing the HTML and that the Java `HttpClient` was configured with strict timeouts to prevent hanging threads.

## 4. Conclusion
AI served as a powerful multiplier for productivity, allowing me to focus on system design, clean code architecture (SOLID principles), and user experience rather than getting bogged down in boilerplate code. I fully understand and can confidently explain every line of code in this repository.
