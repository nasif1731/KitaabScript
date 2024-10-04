# **KitaabScript**

**KitaabScript** is a desktop text editor that supports Arabic-script languages (including Urdu, Arabic, and Pushto). It allows users to write, edit, and export text files using markdown syntax. The editor is equipped with features like pagination, transliteration, content-based search, and file management. **KitaabScript** is designed to be simple, user-friendly, and responsive, providing efficient file handling for large documents.

## **Key Features**

### 1. **Text File Management**
- **Import Text Files:** Users can import text files in Arabic, Urdu, and Pushto scripts. The application supports both single and bulk imports.
- **Export Text Files:** Users can export their edited files in Markdown (`.md`) format for easy viewing in markdown readers.
- **Create, Read, Update, Delete (CRUD) Operations:**
  - Create new text files.
  - View and edit existing text files.
  - Delete text files as needed.

### 2. **Text Editing Features**
- **UTF-8 Encoding and Case Insensitivity:** All text operations use UTF-8 encoding and are case-insensitive.
- **Markdown Support:** Users can apply font properties like bold, italics, text color, and text size using markdown syntax.
- **Auto-Save:** The application automatically saves your progress using a Java scheduler with multithreading.
- **Manual Save:** Users can manually save their work at any time.
- **Find and Replace:** Search for specific text within a file and replace it or perform the operation across the entire database.
- **Content-Based Search:** Search for files based on their content using TF-IDF analysis. The system can suggest keywords for better search results.
- **Pagination:** Large text files are paginated after being saved in the database, allowing for easier navigation.
- **Transliteration:** Convert text between Urdu/Arabic scripts and Roman Arabic.
- **Word Count:** Real-time and post-edit word count display.
- **Basic Shortcuts:** Includes standard editing shortcuts like `Ctrl+C`, `Ctrl+V`, and `Ctrl+X`.

### 3. **File Management**
- **Recently Opened Files:** Quick access to recently opened files.
- **File Browser:** Easily navigate through the file system or database to select text files.

### 4. **Data Analytics**
- **TF-IDF Analysis:** Perform term frequency-inverse document frequency analysis on text files to enhance content-based search.

### 5. **File Integrity**
- **Text File Hashing:** Hashing is used to prevent duplicate imports by comparing file content.

## **System Requirements**
- **Programming Language:** Java
- **UI Framework:** Swing
- **Database:** MariaDB for storing user data, text files, and related metadata.
- **Supported Languages:** Urdu, Arabic, and Pushto (UTF-8 encoded)
- **Operating System Compatibility:** Cross-platform support (Windows, macOS, Linux)

## **Installation**

### 1. **Prerequisites**
- Java Development Kit (JDK) 8 or higher.
- MariaDB installed and configured.

### 2. **Clone the Repository**
```bash
git clone https://github.com/nasif1731/KitaabScript.git
