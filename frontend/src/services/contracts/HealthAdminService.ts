export type uuid = string;

export interface TestAppointment {
  email: string;
  patientName: string;
  startTime: string;
  endTime: string;
  location: string;
  // Appointment type
  apptType: string;
}

export interface ModifyTestAppointment {
  testId: uuid;
  startTime: string;
  endTime: string;
  location: string;
  // Appointment type
  apptType: string;
}

export enum MedicalInfoType {
  COVID = "covid",
  FLU = "flu",
}

export interface FluMedicalInfo {
  testResult: TestResultType;
  daysSick: number;
  haveFever: boolean;
  temperature: number;
}

export interface CovidMedicalInfo {
  testResult: TestResultType;
  vaccineCount: number;
  covidCount: number;
  lungCondition: boolean;
}

export type MedicalInfo = CovidMedicalInfo | FluMedicalInfo;

export interface PatientInfo {
  email: string;
  sex: string;
  dateOfBirth: string;
  state: string;
  city: string;
  zipcode: string;
}

export enum TestResultType {
  POSITIVE = "positive",
  NEGATIVE = "negative",
  PENDING = "pending",
  UNKNOWN = "unknown",
}

export interface TestResult {
  type: MedicalInfoType;
  medicalInfo: MedicalInfo;
  patientInfo: PatientInfo;
}

// This interface is used to provide test results for a given testId
export interface TestResultProvider {
  // Gets the test result for a given testId
  getTestResult(testId: uuid): Promise<TestResult>;
}

export interface HealthAdminService extends TestResultProvider {
  // Registers a test appointment and returns the testId associated with the appointment
  registerTestAppointment(appointment: TestAppointment): Promise<{ testId: uuid }>

  // Modifies a test appointment
  modifyTestAppointment(newAppointment: ModifyTestAppointment): Promise<void>;

  // Deletes the appointment associated with the given testId
  deleteTestAppointment(testId: uuid): Promise<void>;
}
