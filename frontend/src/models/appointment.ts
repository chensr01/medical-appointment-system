// NOTE: We define the interfaces for the appointment and slot objects in a separate file for modularity and reusability.

export interface Slot {
  id: string;
  date: string;
  time: string;
  doctor: string; // doctor name
  location: string;
  type: string; // covid or flu
}

export interface Appointment {
  id: string;
  date: string;    // e.g., 2024-02-03
  time: string;    // e.g., 10:00AM - 10:30AM
  doctor: string;  // doctor name
  location: string;
  type: string; // covid or flu
  testResult: string; // POSITIVE, NEGATIVE, PENDING, UNKNOWN
  quarantineResult: string;
}
