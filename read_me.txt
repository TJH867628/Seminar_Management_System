📌 Prebuilt Core Functions

🔐 Authentication & Session

// Authenticate user (returns Student / Evaluator / Coordinator)
User UserDAO.validateUser(String email, String hashedPassword);

// Set logged-in user (called after login)
UserSession.setUser(User user);

// Get current logged-in user
User UserSession.getUser();

// Clear session (on logout)
UserSession.clear();


⸻

🚦 Navigation

// Open correct dashboard based on user role (with security check)
AppNavigator.openDashboard(User user);

// Logout and return to login page
AppNavigator.logout(JFrame frame);


⸻

🔒 Security

// Check if current user has required role
boolean SecurityUtil.hasRole(String role);

Usage:

if (!SecurityUtil.hasRole("Coordinator")) return;


⸻

💬 Dialogs (UI Messages)

// Show info message
DialogUtil.showInfoDialog(Component parent, String title, String message);

// Show error message
DialogUtil.showErrorDialog(Component parent, String title, String message);

// Show Yes/No confirmation dialog
int DialogUtil.showConfirmDialog(Component parent, String title, String message);


⸻

👤 User Models (Polymorphism)

User
Student
Evaluator
Coordinator

Cast when needed:

Student s = (Student) UserSession.getUser();


⸻

🎯 Dashboard Constructors

new StudentDashboard(Student student);
new EvaluatorDashboard(Evaluator evaluator);
new CoordinatorDashboard(Coordinator coordinator);

⚠️ Do NOT call dashboards directly
Use AppNavigator.openDashboard(user).