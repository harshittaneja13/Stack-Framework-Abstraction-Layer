# CRED Android Assignment

This repository contains an Android application built as part of the initial round assignment for CRED. The project showcases modern Android development techniques using Jetpack Compose and the MVVM architecture.

## Overview

The application demonstrates the following key features:
- **Dynamic Card UI:**  
  Cards that can be expanded to show more details or collapsed to show a summary.
- **Interactive Input Components:**  
  - A simulated dial input using a `Slider` that allows the user to select a numeric value in the range of 500 to 487,891.
  - Radio buttons for selecting options, with the first option set as the default.
- **State Management:**  
  Utilizes a ViewModel along with Compose's reactive state management (`mutableStateOf`) to manage UI updates and user input.
- **Clean Architecture:**  
  Implements the MVVM (Model-View-ViewModel) pattern, separating the UI, business logic, and data models.

## Features

- **Dial Input:**  
  Simulates a rotating dial using a slider that maps its normalized value to a large integer range.
  
- **Card Views:**  
  - **Expanded View:** Displays detailed information and interactive elements.
  - **Collapsed View:** Displays key information along with the current input value.
  
- **Radio Button Groups:**  
  Default selection is set to the first option, and user changes are reflected in the UI immediately.

## Technologies Used

- **Kotlin**
- **Jetpack Compose**
- **ViewModel & LiveData/State**
- **MVVM Architecture**

## Future Improvements

- **UI Enhancements:**  
  More advanced animations and smoother transitions between expanded and collapsed views.
- **Error Handling:**  
  Improved error handling and user feedback mechanisms.

## License

This project is provided for educational and demonstration purposes. Feel free to fork and experiment with the code.

## Contact

For any questions or further discussion, please contact **[Harshit]** at **[harshittaneja.ht4@gmail.com]**.
