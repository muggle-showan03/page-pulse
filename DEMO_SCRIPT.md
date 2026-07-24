# Page Pulse - Demo Script (3-5 Minutes)

## 1. Introduction (0:00 - 0:45)
"Hello everyone, my name is [Your Name], and I'm excited to present my submission for the Digital Heroes Training Task: **Page Pulse**. 
Page Pulse is a full-stack web application designed to instantly analyze any webpage, providing crucial SEO insights, performance metrics, and accessibility checks. I built this to help developers and marketers quickly audit web pages without needing heavy, complex tools."

## 2. Architecture Overview (0:45 - 1:15)
"The application follows a clean, modern client-server architecture. 
- The **backend** is built with Java 21 and Spring Boot. It handles the heavy lifting: validating URLs, securely fetching webpage HTML using the native Java HttpClient, and parsing the DOM using Jsoup.
- The **frontend** is a responsive, single-page application built with React and Vite, utilizing Axios for API communication and a custom CSS design system tailored for a sleek, glassmorphic look."

## 3. Backend Walkthrough (1:15 - 2:00)
"Let's look under the hood at the backend. 
- I strictly adhered to SOLID principles. The `AnalyzeController` is kept incredibly thin—it just maps the HTTP POST request to the service layer.
- The `AnalyzeService` is where the business logic lives. It validates the URL, enforces strict 10-second timeouts on the `HttpClient` to prevent hanging requests, and uses Jsoup to safely extract the page title, meta tags, and header counts.
- I also implemented a `GlobalExceptionHandler` using `@RestControllerAdvice`. This ensures that whether a URL is malformed, a site times out, or the network drops, the frontend always receives a clean, standardized JSON error response with the correct HTTP status code (like 400, 408, or 502)."

## 4. Live Demo & Frontend (2:00 - 3:30)
"Now, let's see it in action.
*(Share screen showing the React frontend)*
- As you can see, the UI is clean and modern. I'll paste in a URL, let's say `https://example.com`, and click Analyze.
- Notice the loading state. 
- The backend processes it, and here are our results: displayed in responsive cards. We see the HTTP status, response time, H1 count, and accessibility issues like images missing alt text.
- Let's test the error handling. If I type in an invalid URL like `not-a-url`, the frontend instantly catches the 400 Bad Request and displays a dismissible red error banner. No crashing, just a smooth user experience."

## 5. Challenges & AI Assistance (3:30 - 4:30)
"One of the main challenges was writing clean, isolated unit tests for the `HttpClient` since it was built internally within the service. 
- I leveraged AI tools like Claude and GitHub Copilot to help brainstorm testing strategies, eventually utilizing Spring's `ReflectionTestUtils` to inject a Mockito mock.
- AI was incredibly helpful as a pair programmer for scaffolding the Vite project and generating CSS boilerplate, which allowed me to focus my time on the core business logic, error handling, and architecture."

## 6. Conclusion (4:30 - 5:00)
"In conclusion, Page Pulse is a robust, tested, and responsive application that fulfills all the internship task requirements. I really enjoyed building this and deepening my knowledge of Spring Boot and React. Thank you for your time, and I'd be happy to answer any questions!"
