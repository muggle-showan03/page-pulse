# Page Pulse - Website Analyzer

Page Pulse is a full-stack web application designed to instantly analyze any webpage, providing valuable SEO insights, performance metrics, and accessibility checks. It was built as a submission for the Digital Heroes Training Task.

## Overview

The application takes a target URL from the user, fetches the HTML content of the page, and extracts key metrics such as the page title, meta description, H1 tag count, image accessibility (missing `alt` attributes), and an approximate word count. It also measures the HTTP response time and status code.

## Live Demo

- **Frontend (Vercel):** [https://page-pulse-ochre.vercel.app/](https://page-pulse-ochre.vercel.app/)
- **Backend API (Render):** [https://page-pulse-jbgc.onrender.com/api/analyze](https://page-pulse-jbgc.onrender.com/api/analyze)

## Features

- **Instant SEO Analysis:** Extracts page titles, meta descriptions, and header tag usage.
- **Accessibility Checks:** Identifies images missing crucial `alt` text.
- **Performance Metrics:** Measures server response time and reports the HTTP status code.
- **Responsive UI:** A modern, glassmorphic design that works seamlessly on desktop, tablet, and mobile.
- **Robust Error Handling:** Gracefully handles invalid URLs, unreachable websites, and request timeouts.

## Tech Stack

**Backend (Spring Boot)**
- Java 21
- Spring Boot 3.x (Web, Validation)
- Jsoup (HTML Parsing)
- Java `HttpClient` (Network requests)
- Lombok (Boilerplate reduction)
- JUnit 5 & Mockito (Unit Testing)

**Frontend (React)**
- React 18 (via Vite)
- Axios (HTTP Client)
- Vanilla CSS (with CSS Variables & Animations)

## Architecture

The project follows a standard client-server architecture:
1. **Client (React):** A single-page application that collects the URL and displays the results.
2. **Controller (Spring Boot):** The `AnalyzeController` exposes a REST endpoint (`POST /api/analyze`).
3. **Service (Spring Boot):** The `AnalyzeService` handles business logic, URL validation, HTTP fetching (via `HttpClient`), and HTML parsing (via `Jsoup`).
4. **Exception Handling:** A `@RestControllerAdvice` globally catches exceptions and maps them to standard JSON error responses.

## Folder Structure

```text
PagePlus/
├── pagepulse/                 # Spring Boot Backend
│   ├── src/main/java/...      # Java Source Code
│   ├── src/test/java/...      # JUnit Tests
│   └── pom.xml                # Maven Dependencies
└── pagepulse-frontend/        # React Frontend
    ├── src/                   # React Source Code
    │   ├── components/        # Reusable UI Components
    │   ├── pages/             # Page Layouts
    │   ├── services/          # API Services (Axios)
    │   └── styles/            # CSS Files
    ├── vite.config.js         # Vite Configuration & Proxy
    └── package.json           # NPM Dependencies
```

## Installation & Setup

### Prerequisites
- Java 21
- Node.js (v18+)
- Maven (Optional, wrapper included)

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd pagepulse
   ```
2. Build the project:
   ```bash
   ./mvnw clean install
   ```
3. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   *The backend will start on `http://localhost:8080`.*

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd pagepulse-frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   npm run dev
   ```
   *The frontend will start on `http://localhost:3000`.*

## API Documentation

### `POST /api/analyze`

Analyzes a given URL and returns SEO and performance metrics.

**Request Body:**
```json
{
  "url": "https://example.com"
}
```

**Success Response (200 OK):**
```json
{
  "analyzedUrl": "https://example.com",
  "httpStatus": 200,
  "responseTimeMs": 142,
  "pageTitle": "Example Domain",
  "metaDescription": "",
  "h1Count": 1,
  "imagesMissingAltCount": 0,
  "wordCount": 42
}
```

**Error Response (400 Bad Request - Invalid URL):**
```json
{
  "timestamp": "2026-07-24T20:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid or unsupported URL: not-a-url",
  "path": "/api/analyze"
}
```

## Running Tests

To run the backend test suite (unit tests and mock MVC tests):
```bash
cd pagepulse
./mvnw test
```

## Future Improvements

- **Caching:** Implement Redis caching to store results for frequently analyzed URLs.
- **Expanded Metrics:** Extract Open Graph tags, check for broken links, and analyze text-to-HTML ratio.
- **History & Authentication:** Allow users to create accounts and save their previous analyses.
- **Frontend Testing:** Add component tests using React Testing Library and Vitest.
