# 🎰 Lotto Project

A lottery application built with **Java**, **Spring Boot**, **MongoDB**, **Redis**, **JWT (RSA256)**, and deployed on **AWS EC2**.

This project implements a complete lottery system where users register, pick their numbers, receive a coupon ID, and wait for automatically scheduled weekly draws.

---

## 🚀 Live Demo

**Application running on EC2:**  
https://ec2-3-122-255-213.eu-central-1.compute.amazonaws.com/

**Frontend Repository:**  
https://github.com/abramovich-maks/FrontendLottery

---

## 📚 API Documentation

**Swagger UI is available at:**  
https://ec2-3-122-255-213.eu-central-1.compute.amazonaws.com:8000/swagger-ui/index.html#

---

## 📌 Project Overview

Users choose **6 unique numbers between 1 and 99**, submit their ticket, and receive a **coupon ID**.  
A scheduler runs every **Saturday at 12:00**, generates winning numbers, checks all coupons, and calculates results.

---

## 🧱 Architecture

The system consists of several components:

- **NumberReceiver** - validates and receives numbers from the user  
- **NumberGenerator** - draws winning numbers for a given date  
- **ResultChecker** - checks how many numbers the user matched  
- **ResultAnnouncer** - returns the result for a given coupon ID  
- **LoginAndRegister** - handles user registration (age validation >=18) and user lookup  
- **JwtAuthenticator** - authenticates users and generates JWT tokens using RSA256  
- **DrowDateFacade** - provides the next draw date  
- **External Random Number API** – https://www.randomnumberapi.com for generating random numbers  

Additional components:

- **Redis** - caches lottery tickets to improve performance  
- **MongoDB** - main database storing users, coupons, and results  

---

## 🔐 Security

- JWT tokens generated with **RSA256**  
- Registration confirmation via **email verification**  
- HTTPS enabled with a custom certificate.p12 configured in the application.

---

## 🗄️ Technologies Used

- **Java 17 / Spring Boot**
- **MongoDB** (main database)
- **Redis** (ticket cache)
- **RSA256 JWT authentication**
- **Spring Scheduler** (runs weekly at Saturday 12:00)
- **Mail service** for registration verification
- **Docker** (optional for local environment)
- **AWS EC2** for deployment

---

## 🧪 Tests

The project includes:

- **Integration tests**
- **Unit tests**

---

## 🎯 How It Works

1. Pick **6 lucky numbers** between 1 and 99  
2. Make sure all numbers are **unique**  
3. Go to the lottery page and submit your ticket  
4. Save your **coupon ID**  
5. Wait for the **Saturday 12:00 draw**  
6. Check your results  

---

## 🗓️ Scheduler

A Spring Scheduler runs **every Saturday at 12:00**, generates 6 random numbers, stores results, and verifies all submitted coupons.

---

## 🖼️ Architecture Diagram  
![Architecture Diagram](architecture/lotto%20architecture%20v2.jpg)

---
