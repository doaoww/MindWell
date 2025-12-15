// Main.java
import java.util.*;
import java.text.SimpleDateFormat;

public class Main {
    public static void main(String[] args) {
        MindWellApp app = MindWellApp.getInstance();
        app.run();
    }
}

// ============= CREATIONAL PATTERN: SINGLETON =============
// Ensures only one instance of the application exists
class MindWellApp {
    private static MindWellApp instance;
    private UserManager userManager;
    private ExerciseManager exerciseManager;
    private CommunityManager communityManager;
    private Scanner scanner;
    private User currentUser;

    private MindWellApp() {
        userManager = UserManager.getInstance();
        exerciseManager = ExerciseManager.getInstance();
        communityManager = CommunityManager.getInstance();
        scanner = new Scanner(System.in);
        initializeDefaultExercises();
    }

    public static MindWellApp getInstance() {
        if (instance == null) {
            instance = new MindWellApp();
        }
        return instance;
    }

    private void initializeDefaultExercises() {
        exerciseManager.addDefaultExercises();
    }

    public void run() {
        System.out.println("╔═══════════════════════════════════╗");
        System.out.println("║     Welcome to MindWell App       ║");
        System.out.println("║  Your Mental Health Companion     ║");
        System.out.println("╚═══════════════════════════════════╝");

        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choose: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                System.out.println("Take care of yourself! Goodbye.");
                System.exit(0);
        }
    }

    private void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (userManager.register(username, password)) {
            System.out.println("✓ Registration successful! Welcome to MindWell.");
        } else {
            System.out.println("✗ Username already exists!");
        }
    }

    private void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = userManager.login(username, password);
        if (currentUser != null) {
            System.out.println("✓ Welcome back, " + username + "!");
        } else {
            System.out.println("✗ Invalid credentials!");
        }
    }

    private void showMainMenu() {
        System.out.println("\n╔═══════════ MAIN MENU ═══════════╗");
        System.out.println("║ 1. Mood Diary                   ║");
        System.out.println("║ 2. Exercises & Meditation       ║");
        System.out.println("║ 3. Community Support            ║");
        System.out.println("║ 4. My Progress & Stats          ║");
        System.out.println("║ 5. Settings & Goals             ║");
        System.out.println("║ 6. Logout                       ║");
        System.out.println("╚═════════════════════════════════╝");
        System.out.print("Choose: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                moodDiaryMenu();
                break;
            case 2:
                exercisesMenu();
                break;
            case 3:
                communityMenu();
                break;
            case 4:
                showProgress();
                break;
            case 5:
                settingsMenu();
                break;
            case 6:
                currentUser = null;
                System.out.println("Logged out. See you soon!");
                break;
        }
    }

    private void moodDiaryMenu() {
        System.out.println("\n=== Mood Diary ===");
        System.out.println("1. Log Today's Mood");
        System.out.println("2. View Mood History");
        System.out.println("3. Get Recommendations");
        System.out.println("4. Back");
        System.out.print("Choose: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                logMood();
                break;
            case 2:
                viewMoodHistory();
                break;
            case 3:
                getRecommendations();
                break;
        }
    }

    private void logMood() {
        System.out.print("Rate your mood (1-10): ");
        int moodLevel = scanner.nextInt();
        scanner.nextLine();

        if (moodLevel < 1 || moodLevel > 10) {
            System.out.println("Please enter a number between 1 and 10.");
            return;
        }

        System.out.print("Any notes about today? ");
        String note = scanner.nextLine();

        MoodEntry entry = new MoodEntry(moodLevel, note);
        currentUser.addMoodEntry(entry);

        System.out.println("✓ Mood logged successfully!");

        if (moodLevel <= 3) {
            System.out.println("\n⚠ We noticed you're feeling low. Would you like some support?");
            System.out.println("Consider trying a relaxation exercise or reaching out to the community.");
        }
    }

    private void viewMoodHistory() {
        System.out.println("\n=== Your Mood History ===");
        List<MoodEntry> history = currentUser.getMoodHistory();

        if (history.isEmpty()) {
            System.out.println("No mood entries yet. Start tracking today!");
            return;
        }

        for (MoodEntry entry : history) {
            System.out.println(entry);
        }

        double avg = history.stream().mapToInt(MoodEntry::getMoodLevel).average().orElse(0);
        System.out.println("\nAverage mood: " + String.format("%.1f", avg) + "/10");
    }

    private void getRecommendations() {
        List<MoodEntry> history = currentUser.getMoodHistory();
        if (history.isEmpty()) {
            System.out.println("Log your mood first to get personalized recommendations!");
            return;
        }

        MoodEntry lastEntry = history.get(history.size() - 1);
        int mood = lastEntry.getMoodLevel();

        // BEHAVIORAL PATTERN: STRATEGY - Select recommendation algorithm
        RecommendationStrategy strategy;
        if (mood <= 3) {
            strategy = new LowMoodStrategy();
        } else if (mood <= 6) {
            strategy = new MediumMoodStrategy();
        } else {
            strategy = new HighMoodStrategy();
        }

        System.out.println("\n" + strategy.getRecommendation());
    }

    private void exercisesMenu() {
        System.out.println("\n=== Exercises & Meditation ===");
        System.out.println("1. Browse Exercises");
        System.out.println("2. Start Exercise");
        System.out.println("3. My Completed Exercises");
        System.out.println("4. Back");
        System.out.print("Choose: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                browseExercises();
                break;
            case 2:
                startExercise();
                break;
            case 3:
                viewCompletedExercises();
                break;
        }
    }

    private void browseExercises() {
        System.out.println("\n=== Available Exercises ===");
        List<Exercise> exercises = exerciseManager.getAllExercises();

        for (int i = 0; i < exercises.size(); i++) {
            Exercise ex = exercises.get(i);
            System.out.println((i + 1) + ". " + ex.getName() + " (" + ex.getDuration() + " min)");
            System.out.println("   " + ex.getDescription());
        }
    }

    private void startExercise() {
        List<Exercise> exercises = exerciseManager.getAllExercises();
        System.out.println("\nSelect exercise:");

        for (int i = 0; i < exercises.size(); i++) {
            System.out.println((i + 1) + ". " + exercises.get(i).getName());
        }

        System.out.print("Choose (0 to cancel): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= exercises.size()) {
            Exercise exercise = exercises.get(choice - 1);

            // STRUCTURAL PATTERN: DECORATOR - Add features to exercise
            Exercise decoratedExercise = new TimerDecorator(new ReminderDecorator(exercise));

            System.out.println("\n" + decoratedExercise.execute());
            currentUser.completeExercise(exercise);
            System.out.println("\n✓ Exercise completed! Great job!");
        }
    }

    private void viewCompletedExercises() {
        System.out.println("\n=== Your Completed Exercises ===");
        List<Exercise> completed = currentUser.getCompletedExercises();

        if (completed.isEmpty()) {
            System.out.println("No exercises completed yet. Start your first one!");
            return;
        }

        List<ExerciseCount> counts = new ArrayList<>();
        for (Exercise ex : completed) {
            boolean found = false;
            for (ExerciseCount ec : counts) {
                if (ec.getName().equals(ex.getName())) {
                    ec.increment();
                    found = true;
                    break;
                }
            }
            if (!found) {
                counts.add(new ExerciseCount(ex.getName()));
            }
        }

        for (ExerciseCount ec : counts) {
            System.out.println("- " + ec.getName() + ": " + ec.getCount() + " times");
        }
    }

    private void communityMenu() {
        System.out.println("\n=== Community Support ===");
        System.out.println("1. View Topics");
        System.out.println("2. Post Message");
        System.out.println("3. My Messages");
        System.out.println("4. Subscribe to Topic");
        System.out.println("5. Back");
        System.out.print("Choose: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                viewTopics();
                break;
            case 2:
                postMessage();
                break;
            case 3:
                viewMyMessages();
                break;
            case 4:
                subscribeToTopic();
                break;
        }
    }

    private void viewTopics() {
        System.out.println("\n=== Community Topics ===");
        List<String> topics = communityManager.getTopics();

        for (String topic : topics) {
            System.out.println("- " + topic);
            List<CommunityPost> posts = communityManager.getPostsByTopic(topic);
            System.out.println("  (" + posts.size() + " posts)");
        }
    }

    private void postMessage() {
        System.out.println("\nAvailable topics:");
        List<String> topics = communityManager.getTopics();

        for (int i = 0; i < topics.size(); i++) {
            System.out.println((i + 1) + ". " + topics.get(i));
        }

        System.out.print("Select topic: ");
        int topicIdx = scanner.nextInt();
        scanner.nextLine();

        if (topicIdx < 1 || topicIdx > topics.size()) {
            System.out.println("Invalid topic.");
            return;
        }

        String topic = topics.get(topicIdx - 1);
        System.out.print("Your message: ");
        String message = scanner.nextLine();

        communityManager.addPost(currentUser, topic, message);
        System.out.println("✓ Message posted!");
    }

    private void viewMyMessages() {
        System.out.println("\n=== Your Posts ===");
        List<CommunityPost> myPosts = communityManager.getPostsByUser(currentUser);

        if (myPosts.isEmpty()) {
            System.out.println("You haven't posted yet.");
            return;
        }

        for (CommunityPost post : myPosts) {
            System.out.println(post);
        }
    }

    private void subscribeToTopic() {
        System.out.println("\nAvailable topics:");
        List<String> topics = communityManager.getTopics();

        for (int i = 0; i < topics.size(); i++) {
            System.out.println((i + 1) + ". " + topics.get(i));
        }

        System.out.print("Select topic to subscribe: ");
        int topicIdx = scanner.nextInt();
        scanner.nextLine();

        if (topicIdx < 1 || topicIdx > topics.size()) {
            System.out.println("Invalid topic.");
            return;
        }

        String topic = topics.get(topicIdx - 1);
        communityManager.subscribe(topic, currentUser);
        System.out.println("✓ Subscribed to: " + topic);
    }

    private void showProgress() {
        System.out.println("\n╔═══════════ YOUR PROGRESS ═══════════╗");

        List<MoodEntry> moods = currentUser.getMoodHistory();
        List<Exercise> exercises = currentUser.getCompletedExercises();

        System.out.println("║ Mood entries: " + moods.size());
        if (!moods.isEmpty()) {
            double avg = moods.stream().mapToInt(MoodEntry::getMoodLevel).average().orElse(0);
            System.out.println("║ Average mood: " + String.format("%.1f", avg) + "/10");
        }

        System.out.println("║ Exercises completed: " + exercises.size());
        System.out.println("╚═════════════════════════════════════╝");
    }

    private void settingsMenu() {
        System.out.println("\n=== Settings & Goals ===");
        System.out.println("1. Set Daily Goal");
        System.out.println("2. View Current Goals");
        System.out.println("3. Back");
        System.out.print("Choose: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                setGoal();
                break;
            case 2:
                viewGoals();
                break;
        }
    }

    private void setGoal() {
        System.out.println("\nSelect your goal:");
        System.out.println("1. Reduce stress");
        System.out.println("2. Improve sleep");
        System.out.println("3. Increase focus");
        System.out.print("Choose: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        String goal = "";
        switch (choice) {
            case 1:
                goal = "Reduce stress";
                break;
            case 2:
                goal = "Improve sleep";
                break;
            case 3:
                goal = "Increase focus";
                break;
        }

        if (!goal.isEmpty()) {
            currentUser.setGoal(goal);
            System.out.println("✓ Goal set: " + goal);
        }
    }

    private void viewGoals() {
        String goal = currentUser.getGoal();
        if (goal == null || goal.isEmpty()) {
            System.out.println("No goals set yet.");
        } else {
            System.out.println("Your current goal: " + goal);
        }
    }
}

// Helper class for counting exercises
class ExerciseCount {
    private String name;
    private int count;

    public ExerciseCount(String name) {
        this.name = name;
        this.count = 1;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        count++;
    }
}

// ============= SINGLETON - UserManager =============
class UserManager {
    private static UserManager instance;
    private List<User> users;

    private UserManager() {
        users = new ArrayList<>();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public boolean register(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        users.add(new User(username, password));
        return true;
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}

// ============= User Class (SOLID: Single Responsibility) =============
class User implements Observer {
    private String username;
    private String password;
    private List<MoodEntry> moodHistory;
    private List<Exercise> completedExercises;
    private String goal;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.moodHistory = new ArrayList<>();
        this.completedExercises = new ArrayList<>();
    }

    public void addMoodEntry(MoodEntry entry) {
        moodHistory.add(entry);
    }

    public void completeExercise(Exercise exercise) {
        completedExercises.add(exercise);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<MoodEntry> getMoodHistory() {
        return moodHistory;
    }

    public List<Exercise> getCompletedExercises() {
        return completedExercises;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getGoal() {
        return goal;
    }

    @Override
    public void update(String topic, String message) {
        System.out.println("\n🔔 Notification: New post in '" + topic + "'");
    }
}

// ============= Mood Entry =============
class MoodEntry {
    private int moodLevel;
    private String note;
    private Date timestamp;

    public MoodEntry(int moodLevel, String note) {
        this.moodLevel = moodLevel;
        this.note = note;
        this.timestamp = new Date();
    }

    public int getMoodLevel() {
        return moodLevel;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String moodIcon = moodLevel <= 3 ? "😔" : moodLevel <= 6 ? "😐" : "😊";
        return "[" + sdf.format(timestamp) + "] " + moodIcon + " " + moodLevel + "/10 - " + note;
    }
}

// ============= BEHAVIORAL PATTERN: STRATEGY =============
// Allows selecting different recommendation algorithms at runtime
interface RecommendationStrategy {
    String getRecommendation();
}

class LowMoodStrategy implements RecommendationStrategy {
    @Override
    public String getRecommendation() {
        return "💙 We're here for you. Try:\n" +
                "- Deep breathing exercise\n" +
                "- Talk to someone in the community\n" +
                "- Take a short walk\n" +
                "- Listen to calming music";
    }
}

class MediumMoodStrategy implements RecommendationStrategy {
    @Override
    public String getRecommendation() {
        return "😊 You're doing okay! Consider:\n" +
                "- A short meditation session\n" +
                "- Journaling your thoughts\n" +
                "- Connect with the community\n" +
                "- Set a small goal for today";
    }
}

class HighMoodStrategy implements RecommendationStrategy {
    @Override
    public String getRecommendation() {
        return "🌟 You're feeling great! Keep it up:\n" +
                "- Share your positive energy in the community\n" +
                "- Try a challenging exercise\n" +
                "- Help someone who might be struggling\n" +
                "- Reflect on what made today special";
    }
}

// ============= SINGLETON - ExerciseManager =============
class ExerciseManager {
    private static ExerciseManager instance;
    private List<Exercise> exercises;

    private ExerciseManager() {
        exercises = new ArrayList<>();
    }

    public static ExerciseManager getInstance() {
        if (instance == null) {
            instance = new ExerciseManager();
        }
        return instance;
    }

    public void addDefaultExercises() {
        exercises.add(new BreathingExercise());
        exercises.add(new MeditationExercise());
        exercises.add(new RelaxationExercise());
        exercises.add(new MindfulnessExercise());
    }

    public List<Exercise> getAllExercises() {
        return exercises;
    }
}

// ============= Exercise Classes (SOLID: Open/Closed Principle) =============
abstract class Exercise {
    protected String name;
    protected String description;
    protected int duration;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public abstract String execute();
}

class BreathingExercise extends Exercise {
    public BreathingExercise() {
        this.name = "Deep Breathing";
        this.description = "Calm your mind with breathing";
        this.duration = 5;
    }

    @Override
    public String execute() {
        return "🌬 Deep Breathing Exercise:\n" +
                "1. Breathe in deeply for 4 seconds\n" +
                "2. Hold for 4 seconds\n" +
                "3. Breathe out for 4 seconds\n" +
                "4. Repeat 5 times";
    }
}

class MeditationExercise extends Exercise {
    public MeditationExercise() {
        this.name = "Guided Meditation";
        this.description = "Find your inner peace";
        this.duration = 10;
    }

    @Override
    public String execute() {
        return "🧘 Guided Meditation:\n" +
                "1. Sit comfortably\n" +
                "2. Close your eyes\n" +
                "3. Focus on your breath\n" +
                "4. Let thoughts pass without judgment\n" +
                "5. Stay present for 10 minutes";
    }
}

class RelaxationExercise extends Exercise {
    public RelaxationExercise() {
        this.name = "Progressive Relaxation";
        this.description = "Release muscle tension";
        this.duration = 15;
    }

    @Override
    public String execute() {
        return "💆 Progressive Relaxation:\n" +
                "1. Start with your toes, tense for 5 seconds\n" +
                "2. Release and feel the relaxation\n" +
                "3. Move up to calves, thighs, etc.\n" +
                "4. Continue until you reach your head";
    }
}

class MindfulnessExercise extends Exercise {
    public MindfulnessExercise() {
        this.name = "Mindfulness Practice";
        this.description = "Be present in the moment";
        this.duration = 7;
    }

    @Override
    public String execute() {
        return "🌸 Mindfulness Practice:\n" +
                "1. Notice 5 things you can see\n" +
                "2. Notice 4 things you can touch\n" +
                "3. Notice 3 things you can hear\n" +
                "4. Notice 2 things you can smell\n" +
                "5. Notice 1 thing you can taste";
    }
}

// ============= STRUCTURAL PATTERN: DECORATOR =============
// Dynamically adds features (timer, reminder) to exercises
abstract class ExerciseDecorator extends Exercise {
    protected Exercise wrappedExercise;

    public ExerciseDecorator(Exercise exercise) {
        this.wrappedExercise = exercise;
        this.name = exercise.getName();
        this.description = exercise.getDescription();
        this.duration = exercise.getDuration();
    }

    @Override
    public String execute() {
        return wrappedExercise.execute();
    }
}

class TimerDecorator extends ExerciseDecorator {
    public TimerDecorator(Exercise exercise) {
        super(exercise);
    }

    @Override
    public String execute() {
        return "⏱ Timer: " + duration + " minutes\n" +
                "────────────────────────\n" +
                wrappedExercise.execute() +
                "\n────────────────────────\n" +
                "✓ Time's up!";
    }
}

class ReminderDecorator extends ExerciseDecorator {
    public ReminderDecorator(Exercise exercise) {
        super(exercise);
    }

    @Override
    public String execute() {
        return "🔔 Reminder: Take your time and breathe\n" +
                wrappedExercise.execute();
    }
}

// ============= BEHAVIORAL PATTERN: OBSERVER =============
// Notifies users when new posts are made in subscribed topics
interface Observer {
    void update(String topic, String message);
}

interface Subject {
    void subscribe(String topic, Observer observer);
    void unsubscribe(String topic, Observer observer);
    void notifyObservers(String topic, String message);
}

class TopicSubscribers {
    private String topic;
    private List<Observer> observers;

    public TopicSubscribers(String topic) {
        this.topic = topic;
        this.observers = new ArrayList<>();
    }

    public String getTopic() {
        return topic;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
}

// ============= SINGLETON - CommunityManager =============
class CommunityManager implements Subject {
    private static CommunityManager instance;
    private List<CommunityPost> posts;
    private List<TopicSubscribers> topicSubscribers;
    private List<String> topics;

    private CommunityManager() {
        posts = new ArrayList<>();
        topicSubscribers = new ArrayList<>();
        topics = new ArrayList<>();

        topics.add("Stress & Anxiety");
        topics.add("Sleep Issues");
        topics.add("Self-Development");
        topics.add("Depression Support");

        for (String topic : topics) {
            topicSubscribers.add(new TopicSubscribers(topic));
        }
    }

    public static CommunityManager getInstance() {
        if (instance == null) {
            instance = new CommunityManager();
        }
        return instance;
    }

    public void addPost(User user, String topic, String message) {
        CommunityPost post = new CommunityPost(user, topic, message);
        posts.add(post);
        notifyObservers(topic, message);
    }

    public List<String> getTopics() {
        return topics;
    }

    public List<CommunityPost> getPostsByTopic(String topic) {
        List<CommunityPost> result = new ArrayList<>();
        for (CommunityPost post : posts) {
            if (post.getTopic().equals(topic)) {
                result.add(post);
            }
        }
        return result;
    }

    public List<CommunityPost> getPostsByUser(User user) {
        List<CommunityPost> result = new ArrayList<>();
        for (CommunityPost post : posts) {
            if (post.getUser().equals(user)) {
                result.add(post);
            }
        }
        return result;
    }

    @Override
    public void subscribe(String topic, Observer observer) {
        for (TopicSubscribers ts : topicSubscribers) {
            if (ts.getTopic().equals(topic)) {
                ts.addObserver(observer);
                break;
            }
        }
    }

    @Override
    public void unsubscribe(String topic, Observer observer) {
        for (TopicSubscribers ts : topicSubscribers) {
            if (ts.getTopic().equals(topic)) {
                ts.removeObserver(observer);
                break;
            }
        }
    }

    @Override
    public void notifyObservers(String topic, String message) {
        for (TopicSubscribers ts : topicSubscribers) {
            if (ts.getTopic().equals(topic)) {
                for (Observer observer : ts.getObservers()) {
                    observer.update(topic, message);
                }
                break;
            }
        }
    }
}

class CommunityPost {
    private User user;
    private String topic;
    private String message;
    private Date timestamp;

    public CommunityPost(User user, String topic, String message) {
        this.user = user;
        this.topic = topic;
        this.message = message;
        this.timestamp = new Date();
    }

    public User getUser() {
        return user;
    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return "[" + topic + "] " + user.getUsername() + " (" + sdf.format(timestamp) + "):\n" + message;
    }
}