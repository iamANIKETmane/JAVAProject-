# 🧭 Real-Time Data Visualization Dashboard

A **Spring Boot 3.2 (Java 21)** enterprise-grade web application for **real-time analytics, visualization, and monitoring**.  
Features a live dashboard powered by WebSockets, REST APIs, and dynamic front-end charts built with **Chart.js**, **Bootstrap 5**, and **Thymeleaf**.

---

## 🌟 Key Features

- 🔄 **Real-time updates** using WebSocket / STOMP (SockJS fallback)
- 📈 **Interactive charts** (line, bar, pie, area, gauge, heatmap, trend analysis)
- 🧮 **Live analytics** — average, standard deviation, min/max, hourly and category-wise aggregation
- 🧰 **Data management UI** with full CRUD operations, filtering, and search
- 🗃️ **Persistence layer** with Spring Data JPA (H2 development / MySQL production)
- 🕹️ **Configurable dashboards** with multiple visualization types
- 🧠 **Advanced statistics** with real-time calculations and trend detection
- 🎨 **Responsive UI** with Bootstrap 5, custom CSS, and professional animations
- ⚙️ **Actuator endpoints** for health monitoring and metrics
- 🧭 **Multi-page application** with dedicated views for Dashboard, Charts, and Data Management

---

## 📊 Dashboard Data Overview

The application automatically loads **comprehensive test data** on startup to demonstrate its full capabilities. The dashboard displays real-time metrics across multiple business domains:

### Data Categories

| Category | Metrics | Time Span | Update Frequency |
|:---------|:--------|:----------|:-----------------|
| **Sales** | Hourly Revenue ($) | Last 24 hours | Hourly |
| **Orders** | Order Count | Last 24 hours | Hourly |
| **Website Traffic** | Page Views, Unique Visitors, Bounce Rate (%) | Last 24 hours | Every 2 hours |
| **Server Performance** | CPU Usage (%), Memory Usage (%), Response Time (ms) | Last 24 hours | Hourly |
| **User Engagement** | Active Users, Session Duration (min), Conversion Rate (%) | Last 7 days | Daily |
| **Network Traffic** | Bandwidth Usage (Mbps), Request Count | Last 24 hours | Every 3 hours |
| **Revenue** | Sales by Product Category (Electronics, Clothing, Books, Home & Garden, Sports) | Last 7 days | Daily |
| **Customer Satisfaction** | NPS Score, Support Tickets | Last 30 days | Every 3 days |

### Initial Dataset Statistics
- **Total Data Points**: 334+ records loaded on startup
- **Time Range**: Last 30 days of historical data
- **Categories**: 8 distinct business metrics categories
- **Real-time Generation**: Continuous data generation every 5 seconds after startup
- **Data Sources**: E-commerce Platform, Web Analytics, Monitoring, CRM, Network Monitoring, Sales System, Customer Feedback

### Sample Dashboard Views

The dashboard provides three main pages:

1. **Main Dashboard** (`/dashboard`)
   - Real-time statistics cards (Total Data Points, Active Categories, Real-Time Updates)
   - Live trend chart with WebSocket updates
   - Category-wise distribution visualization
   - System status monitoring

2. **Advanced Charts** (`/charts`)
   - Primary interactive chart with zoom controls
   - Distribution analysis (pie/doughnut charts)
   - Comparison charts for category analysis
   - Trend detection and forecasting
   - Heatmap visualization
   - Gauge charts for KPIs
   - Filters: Chart Type, Category, Time Range, Aggregation Method

3. **Data Management** (`/data`)
   - Comprehensive data table with pagination
   - CRUD operations (Create, Read, Update, Delete)
   - Advanced filtering and search
   - Bulk data generation
   - JSON export/import functionality
   - Real-time statistics dashboard

**Note**: Screenshots can be inserted below to showcase the actual dashboard interface.

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

## 🎥 Demo Video

Watch the complete walkthrough and live demonstration:

[![Demo Video](https://drive.google.com/file/d/1ah_8b0JLTNpiKmLxSAMK64tNUPdJH8SQ/view?usp=drive_link)]

*📹 2-minute demo showcasing real-time updates, interactive charts, and data management features*




## 🔧 Technical Implementation Details

### Architecture
- **Backend**: RESTful APIs with Spring Boot 3.2
- **WebSocket**: STOMP over SockJS for real-time bi-directional communication
- **Database**: JPA/Hibernate with H2 (dev) and MySQL (production) support
- **Frontend**: Server-side rendering with Thymeleaf + client-side JavaScript

### Key Components
1. **DataInitializer**: Loads 334+ test data points on startup spanning 30 days
2. **DataGeneratorService**: Scheduled service generating new data every 5 seconds
3. **WebSocketConfig**: Configures STOMP messaging with fallback support
4. **DataPointService**: Business logic layer handling data operations and WebSocket broadcasts
5. **RealTimeDashboard (JS)**: Client-side class managing WebSocket connections and chart updates

### Database Schema
- **data_points**: Main table storing time-series data with category, value, timestamp, unit, source, and metadata
- **dashboards**: Stores dashboard configurations and layouts
- **charts**: Stores individual chart configurations

---

## Future Enhancements

- 🔐 User authentication and role-based access control
- 📧 Email/SMS alerts for threshold violations
- 📱 Mobile-responsive progressive web app (PWA)
- 🌐 Multi-language support (i18n)
- 📊 Advanced ML-based forecasting and anomaly detection
- 💾 Support for additional databases (PostgreSQL, MongoDB)
- 🔄 Data streaming from external sources (Kafka, RabbitMQ)
- 📈 Custom dashboard builder with drag-and-drop widgets



## Authors

**Aniket Mane**  
[GitHub Profile](https://github.com/iamANIKETmane)

**Aneesh Mokashi**  
[GitHub Profile](https://github.com/aneeshKm)

**Princy Doshi**  


