🧠 MindWell – Mental Health Console Application

MindWell is a console-based mental health application that helps users track their mood, practice guided exercises, and connect with community support. It is designed as an educational demonstration of software design patterns and SOLID principles in Java.

---

## ✨ Features

- **Mood Tracking** – Daily mood logging (1–10) with notes and history view  
- **Guided Exercises** – Breathing, Meditation, Relaxation, and Mindfulness  
- **Personalized Recommendations** – Based on current mood level  
- **Community Forums** – Topic-based posts with subscription notifications  
- **Progress Statistics** – Mood averages and exercise completion history  
- **Goal Setting** – Reduce stress, improve sleep, increase focus  

---

## 🛠️ Technical Stack

- **Language:** Java  
- **Data Structure:** ArrayList  
- **Interface:** Console-based  
- **Design Patterns:** Singleton, Decorator, Strategy  
- **Principles:** SOLID (SRP, OCP, LSP, ISP, DIP)

---

## 📁 Project Structure (Key Classes)

```
MindWell/
├── Main                     # Entry point
├── MindWellApp              # Main controller (Singleton)
├── User                     # User data model
├── MoodEntry                # Mood record model
├── UserManager              # Authentication (Singleton)
├── ExerciseManager          # Exercise library (Singleton)
├── CommunityManager         # Community features (Singleton)
├── Exercise (abstract)      # Base exercise class
│   ├── BreathingExercise
│   ├── MeditationExercise
│   ├── RelaxationExercise
│   └── MindfulnessExercise
├── ExerciseDecorator (abstract)
│   ├── TimerDecorator
│   └── ReminderDecorator
├── RecommendationStrategy (interface)
│   ├── LowMoodStrategy
│   ├── MediumMoodStrategy
│   └── HighMoodStrategy
└── Observer / Subject       # For community notifications
```

---

## 🧩 Design Patterns Used

| Pattern | Category | Purpose |
|---------|----------|---------|
| **Singleton** | Creational | Single instance of managers (UserManager, ExerciseManager, etc.) |
| **Decorator** | Structural | Add timers/reminders to exercises dynamically |
| **Strategy** | Behavioral | Change recommendation logic based on mood level |

---

## ✅ SOLID Principles Applied

- **Single Responsibility** – Each class handles one concern  
- **Open/Closed** – New exercises/strategies added without modifying existing code  
- **Liskov Substitution** – All exercises work through base `Exercise` reference  
- **Interface Segregation** – Small, focused interfaces (`Observer`, `RecommendationStrategy`)  
- **Dependency Inversion** – Code depends on abstractions, not concrete classes  

---

## 🚀 Getting Started

### Prerequisites

- Java JDK 8 or higher  
- Command line / terminal  

### Installation & Run

```bash
# Compile all Java files
javac Main.java

# Run the application
java Main
```

> 💾 **Note:** Data is stored in-memory only and will reset when the program stops (intentional for academic simplicity).

---

## 🧪 Testing Overview

### Functional Tests
- Registration (valid users accepted, duplicates rejected)
- Login validation
- Mood logging and low‑mood warnings
- Exercise execution with decorators
- Community post notifications

### Pattern Tests
- Singleton maintains one instance
- Decorator chains timers and reminders
- Strategy selects correct mood-based advice
- Observer notifies all subscribers

### Integration Tests
- Full user journey from registration → mood logging → exercises → community → stats

---

## 📖 User Guide (Quick Start)

1. **First use** – Register with a username and password  
2. **Login** – Enter your credentials  
3. **Mood Diary** – Log daily mood (1–10) + notes, view history & average, get recommendations  
4. **Exercises** – Browse, start with optional timer/reminder, view completion history  
5. **Community** – View topics, post messages, subscribe for notifications  
6. **Progress** – View mood statistics and exercise counts  
7. **Settings** – Set personal goals  

---

## 📌 Technical Decisions

| Decision | Reason |
|----------|--------|
| `ArrayList` over `HashMap` | Simpler for educational context; adequate for expected data size |
| Console interface | Focus on design patterns, not UI complexity |
| In‑memory storage | Easy implementation for academic demonstration |

---

## 🤝 Contributing

This project is intended as a learning demonstration. For educational use or improvement suggestions, feel free to open an issue or fork the repository.

## 👏 Acknowledgments

Built to demonstrate practical application of design patterns and SOLID principles in a real‑world (simplified) mental health tool context.
