# GroupProjectCSC584

# Uni-Run: Marathon & Virtual Run Coordinator
Java Web Application (JSP + Servlet + JavaBean, MVC architecture) using JavaDB (Apache Derby)
Built for NetBeans IDE 8.2

## 1. What this project contains
- **Model (JavaBean + DAO)**: `src/java/model/*.java`, `src/java/dao/*.java`, `src/java/util/DBConnection.java`
- **View (JSP)**: everything under `web/` (login.jsp, register.jsp, dashboard.jsp, events/, registrations/)
- **Controller (Servlet)**: `src/java/controller/*.java`
- **Filters (session/role control)**: `src/java/filter/*.java`
- **Database script**: `sql/create_database.sql`

## 2. The four required modules
| # | Requirement            | Where it lives |
|---|-------------------------|----------------|
| a | Login/Logout (session) | `LoginServlet`, `LogoutServlet`, `AuthFilter`, `login.jsp` |
| b | Registration            | `RegisterServlet`, `register.jsp` |
| c | Information Mgmt (CRUD) | `EventServlet` + `events/list.jsp` + `events/form.jsp` (manages Run Events); `RegistrationServlet` + `registrations/*.jsp` (participants register/cancel, admin confirms/deletes) |
| d | Dashboard                | `DashboardServlet`, `dashboard.jsp` |

## 3. Database design (4 tables, for your ERD/storyboard deliverable)
- **USERS** (USER_ID, USERNAME, PASSWORD, FULL_NAME, EMAIL, PHONE, ROLE, REGISTER_DATE)
- **CATEGORIES** (CATEGORY_ID, CATEGORY_NAME, DISTANCE_KM, FEE)
- **RUN_EVENTS** (EVENT_ID, EVENT_NAME, EVENT_DATE, LOCATION, CATEGORY_ID→CATEGORIES, DESCRIPTION, STATUS, CREATED_BY→USERS)
- **REGISTRATIONS** (REGISTRATION_ID, USER_ID→USERS, EVENT_ID→RUN_EVENTS, BIB_NUMBER, REGISTRATION_DATE, STATUS)

Relationships: USERS (1)—(M) RUN_EVENTS (created_by, admin only); CATEGORIES (1)—(M) RUN_EVENTS; USERS (1)—(M) REGISTRATIONS; RUN_EVENTS (1)—(M) REGISTRATIONS.

## 4. How to set this up in NetBeans IDE 8.2 (step-by-step)

### Step A — Create the JavaDB database
1. Open NetBeans → **Services** tab → **Databases** → right-click **Java DB** → **Start Server** (if not already running).
2. Right-click **Java DB** → **Create Database…**
   - Database Name: `UniRunDB`
   - User Name: `app`
   - Password: `app`
3. This creates a connection like `jdbc:derby://localhost:1527/UniRunDB`. It should appear under Databases automatically (right-click it → Connect if not already connected).
4. Right-click the new connection → **Execute Command…** → open `sql/create_database.sql` from this project (or paste its contents) → run it (▷ Run SQL). This creates the 4 tables and inserts sample data (an admin account `admin`/`admin123` and a participant `john`/`john123`).

### Step B — Create the NetBeans project and copy in the code
1. In NetBeans: **File → New Project → Java Web → Web Application**. Name it `UniRun`.
2. Choose your server (GlassFish or Tomcat — whichever ships with your NetBeans 8.2 install) and Java EE version 6 or 7.
3. Once the project is created, NetBeans generates its own `src/java`, `web`, `nbproject`, `build.xml`, etc.
4. **Copy the contents of this package's `src/java/` folder into your new project's `src/java/` folder**, and **copy the contents of this package's `web/` folder into your new project's `web/` folder** (overwrite the default `index.jsp` — that's expected). You can drag-and-drop in your OS file explorer directly into the NetBeans project folder, then right-click the project in NetBeans → **Reload**.

### Step C — Add the Derby Client driver to the project
1. Right-click the project → **Properties → Libraries → Add Library…**
2. Add **Java DB Driver** (or **Derby Client**, depending on your NetBeans distribution) — this provides `org.apache.derby.jdbc.ClientDriver`.
   - If you don't see it listed, add it as a JAR: it's usually located at `<NetBeans install dir>/javadb/lib/derbyclient.jar` (or `<JDK dir>/db/lib/derbyclient.jar`).

### Step D — Run
1. Make sure the Java DB server is still running (Step A).
2. Right-click the project → **Clean and Build**, then **Run**.
3. Your browser should open to `login.jsp`. Log in with:
   - **Admin**: `admin` / `admin123` (can manage Run Events, confirm/delete registrations)
   - **Participant**: `john` / `john123` (can browse events, register, view own registrations)
   - Or click **Register here** to create a new participant account.

## 5. Notes / possible extensions
- Passwords are stored as plain text for simplicity, matching typical student-project scope. For a production system you'd hash them (e.g. BCrypt).
- Styling uses Bootstrap 5 + Font Awesome via CDN — an internet connection is needed in the browser for the icons/styling to load (functionality still works offline, just plainer).
- You can extend the CRUD module further (e.g. add a Category management screen) using the same DAO/Servlet/JSP pattern shown in `EventServlet` / `events/*.jsp`.
