# 🧭 Real-Time Data Visualization Dashboard

A **Spring Boot 3.2 (Java 21)** web application for **real-time analytics, visualization, and monitoring**.  
It features a live dashboard powered by WebSockets, REST APIs, and dynamic front-end charts built with **Chart.js**, **Bootstrap 5**, and **Thymeleaf**.

---

## 🌟 Features

- 🔄 **Real-time updates** using WebSocket / STOMP (SockJS fallback)
- 📈 **Interactive charts** (trend, bar, pie, area, gauge, heatmap)
- 🧮 **Live analytics** — average, std. dev, min/max, hourly and category-wise aggregation
- 🧰 **Data management UI** (CRUD, CSV import/export)
- 🗃️ **Persistence layer** with Spring Data JPA (H2 dev / MySQL prod)
- 🕹️ **Configurable layout** stored in the database
- 🧠 **Statistics, controls, and filters** for categories, ranges, and aggregations
- 🎨 **Responsive UI** with Bootstrap 5, custom CSS, dark-mode, and animations
- ⚙️ **Actuator endpoints** (`/actuator/health`, `/actuator/metrics`)
- 🧭 **Thymeleaf templates** for pages: `/dashboard`, `/charts`, `/data`, `/settings`, `/about`

---

## 🏗️ Tech Stack

| Layer | Technologies |
|:------|:--------------|
| **Backend** | Spring Boot 3.2, Spring Web, WebSocket, Spring Data JPA, Spring Validation, Spring Actuator |
| **Database** | H2 (in-memory dev), MySQL (optional prod) |
| **Frontend** | Thymeleaf templates, Bootstrap 5, Chart.js v4, Font Awesome, custom CSS / JS |
| **Build Tool** | Maven |
| **Language** | Java 21 |

---

## 🗂️ Project Structure

```
src/main/java/com/dashboard
├── config/
│   └── WebSocketConfig.java
├── controller/
│   ├── DashboardWebController.java
│   ├── DataPointController.java
│   └── WebSocketController.java
├── model/
│   ├── Chart.java
│   ├── Dashboard.java
│   └── DataPoint.java
├── repository/
│   ├── ChartRepository.java
│   ├── DashboardRepository.java
│   └── DataPointRepository.java
└── DashboardApplication.java
```

```
src/main/resources
├── static/
│   ├── css/
│   │   └── dashboard.css
│   └── js/
│       ├── dashboard.js
│       └── charts.js
├── templates/
│   ├── dashboard.html
│   ├── charts.html
│   └── data.html
└── application.properties
```

---

## ⚙️ Configuration (application.properties)

Default setup uses **H2** (in-memory) for local development:

```properties
spring.datasource.url=jdbc:h2:mem:dashboard
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.hibernate.ddl-auto=create-drop
spring.thymeleaf.cache=false
server.port=8080
```

### 🗄️ MySQL (production example)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dashboard
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

---

## 🚀 Running the Application

### 🧩 Prerequisites
- JDK 21+
- Maven 3.8+

### 🔧 Build & Run
```bash
# clone and enter
git clone https://github.com/iamANIKETmane/JAVAProject-.git
cd JAVAProject-

# build and package
mvn clean package

# run
java -jar target/realtime-data-visualization-dashboard-1.0-SNAPSHOT.jar
```

### 💻 Development mode
```bash
mvn spring-boot:run
```

Access:
- Dashboard → http://localhost:8080/dashboard  
- Charts → http://localhost:8080/charts  
- Data Management → http://localhost:8080/data  
- H2 Console → http://localhost:8080/h2-console

---

## 🧩 REST API Overview

| Endpoint | Method | Description |
|:----------|:-------|:-------------|
| `/api/datapoints/recent` | `GET` | Latest 100 data points |
| `/api/datapoints/categories` | `GET` | Distinct categories |
| `/api/datapoints/aggregated` | `GET` | Aggregated (sum/count) data |
| `/api/datapoints/hourly/{category}` | `GET` | Hourly average data per category |
| `/api/datapoints/search` | `GET` | Search by label / description |
| `/api/datapoints/generate/{n}` | `POST` | Generate sample data |
| `/topic/datapoints` | WS | Stream real-time data points |
| `/topic/system/status` | WS | Broadcast system health updates |

---

## 📊 WebSocket Endpoints

| Path | Type | Description |
|:-----|:-----|:-------------|
| `/ws` | SockJS STOMP endpoint |
| `/ws-native` | Native WebSocket endpoint |
| `/topic/datapoints` | Broadcast new data points |
| `/topic/notifications` | System messages / alerts |
| `/topic/system/status` | Live status and errors |

---

## 🧠 Front-End Architecture

| File | Purpose |
|:-----|:---------|
| **`dashboard.js`** | Handles WebSocket connection (STOMP / SockJS), real-time data updates, and live charts. |
| **`charts.js`** | Manages advanced visualizations (heatmap, gauge, distribution, trend), filters, zoom, and export. |
| **`dashboard.css`** | Custom theme with gradients, animations, status indicators, and dark-mode support. |
| **Thymeleaf HTMLs** | Templates for Dashboard, Charts, and Data pages. |

Front-end stack includes:
- **Chart.js v4**
- **Bootstrap 5.3**
- **Font Awesome 6**
- **SockJS / STOMP JS**
- **Thymeleaf templating**

---

## 👨‍💻 Authors
**Aniket Mane**  
[GitHub Profile](https://github.com/iamANIKETmane)

**Aneesh Mokashi**  
[GitHub Profile](https://github.com/aneeshKm)

**Princy Doshi**  

---
