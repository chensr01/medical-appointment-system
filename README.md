# Medical Appointment System

This repository contains the codebase for the **Medical Appointment System**, a comprehensive, full-stack web application developed as part of the course **CMU 17-732 Designing Large Scale Software Systems**. The system is designed to support subservices for both patients and healthcare professionals, providing features for scheduling, patient record management, and policy compliance.

## Overview

The **Medical Appointment System** includes the following components:

- **Medical Appointment Scheduling App**: Allows patients to make, view, modify, and cancel testing appointments.
- **Healthcare Administrator Service**: Enables healthcare professionals to input test results and other relevant medical information.
- **Policy Maker Service**: Provides government officials with tools to modify quarantine policies based on patient health conditions.
- **Central Patient Database Service**: Centralized storage and retrieval for patient information across multiple scheduling apps.
- **Public Information Service**: Allows users to access aggregated health statistics and data for general awareness.

This repository includes:
- **Medical Appointment Scheduling App**: Located in the `frontend` and `backend` folders.
- **Healthcare Administrator Service**: Located in the `healthAdmin` folder.

## Tech Stack

- **Frontend**: React.js, Material UI templates, TypeScript
- **Backend**: Spring Boot with Java
- **Database**: MongoDB

## Deployment

- The application is deployed on a CMU virtual machine, using **NGINX** as a reverse proxy to handle requests efficiently.

## Testing

End-to-end testing is performed using **Playwright** to ensure a smooth user experience and validate critical workflows across all services.

## Folder Structure

- **frontend**: Contains the frontend code for the medical appointment scheduling app.
- **backend**: Contains the backend code for the medical appointment scheduling app.
- **healthAdmin**: Contains the code for the healthcare administrator service.
