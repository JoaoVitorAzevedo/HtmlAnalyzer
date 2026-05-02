# HtmlAnalyzer

## Overview
HtmlAnalyzer is a lightweight Java tool designed to analyze the structure of HTML pages from a given URL. Its primary function is to identify and retrieve the text content located at the deepest level of nesting within the document's tag hierarchy.

This project was built with a focus on **Clean Code** principles, algorithmic efficiency, and low memory footprint, utilizing only the standard JDK without any external dependencies.

## Problem Description
The goal is to navigate through an HTML structure and find the piece of text that is most deeply nested. For example, in the following structure:

```html
<html>
  <head>
    <title>
      Deepest Text
    </title>
  </head>
  <body>
    Body Text
  </body>
</html>
```

The string `"Deepest Text"` is at depth 3 (`html > head > title`), while `"Body Text"` is at depth 2 (`html > body`). Therefore, the program should return `"Deepest Text"`.

### Technical Constraints & Assumptions
To keep the parser focused on the core logic of tree traversal and depth analysis, the project operates under the following structural assumptions:
1.  **Line-based Input:** The HTML is processed line by line. Each line contains either an opening tag, a closing tag, or a text segment.
2.  **No Mixed Lines:** A single line will not contain more than one type of content (e.g., a tag and text together).
3.  **Simple Tags:** Only standard opening (`<div>`) and closing (`</div>`) tags are used. Self-closing tags (like `<br/>`) and tag attributes (like `<div class="container">`) are not expected.
4.  **Tie-breaking Rule:** If multiple text segments are found at the same maximum depth, the application returns the first one encountered.

## How It Works
The application uses a **Stack-based approach** combined with linear scanning to track depth and validate the structure.

1.  **Stream Processing:** The program reads the HTML source line by line, ensuring efficiency even for larger documents.
2.  **Depth Tracking:**
    * **Opening Tags:** Increments the depth counter and pushes the tag onto a validation Stack.
    * **Closing Tags:** Decrements the depth and pops from the Stack.
    * **Text Segments:** Compares the current depth with the max depth found so far and updates the result accordingly.
3.  **Structural Validation:** The Stack-based logic ensures that every closing tag matches the most recently opened one. If a mismatch or stack underflow is detected, the program identifies the HTML as **malformed**.

## Key Features
* **Deepest Text Identification:** Accurately finds the most nested text in the DOM tree.
* **Malformed HTML Detection:** Identifies unclosed tags, mismatched tags, or invalid nesting.
* **Zero Dependencies:** Built entirely with standard Java (JDK 17+).
* **Robust Error Handling:** Manages connection timeouts, HTTP errors, and invalid URLs.

## Usage

### 1. Compilation
```bash
javac HtmlAnalyzer.java
```

### 2. Execution
```bash
java HtmlAnalyzer <URL>
```

#### Example:
```bash
java HtmlAnalyzer https://www.example.com/sample.html
```

### Expected Output
1.  **The deepest text found.**
2.  `malformed HTML` (if the document structure is invalid).
3.  `URL connection error` (if the URL is unreachable or invalid).

---
Author: [João Vitor Azevedo](https://github.com/JoaoVitorAzevedo)
