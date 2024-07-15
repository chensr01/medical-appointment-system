export interface PatientAndCovidInfo {
  apptType: string;
  city: string;
  state: string;
  zipcode: string;
  dob: string;
  gender: string;
  vaccinationCount: number;
  diseaseCount: number;
  lungCondition: boolean;
}

export interface PatientAndFluInfo {
  apptType: string;
  city: string;
  state: string;
  zipcode: string;
  dob: string;
  gender: string;
  daysSick: number;
  haveFever: boolean;
  temperature: number;
}

export type PatientAndTestInfo = PatientAndCovidInfo | PatientAndFluInfo;

export interface QuarantineResult {
  duration: number;   // number of days to quarantine
}

export interface QuarantineResultProvider {
  getQuarantineResult(patientAndTestInfo: PatientAndTestInfo): Promise<QuarantineResult>;
}
