import {
  CovidMedicalInfo,
  FluMedicalInfo,
  MedicalInfo,
  MedicalInfoType,
  PatientInfo,
  TestResult,
  TestResultProvider,
  TestResultType,
  uuid
} from "../contracts/HealthAdminService";
import { randomEnum } from "../../utils/Utils";

export class RandomTestResultProvider implements TestResultProvider {
  private testResults: Map<uuid, TestResult>;

  constructor() {
    this.testResults = new Map();
  }

  async getTestResult(testId: uuid): Promise<TestResult> {
    const result = this.testResults.get(testId);
    // If the test result is not found, generate a random one
    if (result === undefined) {
      const randomResult = this.generateRandomTestResult();
      this.testResults.set(testId, randomResult);

      return randomResult;
    }
    // Else, return the test result
    return result;
  }

  generateRandomTestResult(): TestResult {
    let medicalInfo = this.generateRandomMedicalInfo();
    let patientInfo = this.getPseudoPatientInfo();
    return {
      type: medicalInfo.type,
      medicalInfo: medicalInfo.info,
      patientInfo: patientInfo
    };
  }

  generateRandomMedicalInfo(): { type: MedicalInfoType, info: MedicalInfo } {
    let fluMedicalInfo: FluMedicalInfo = {
      testResult: randomEnum(TestResultType),
      daysSick: Math.floor(Math.random() * 14) + 1,
      haveFever: Math.random() < 0.5,
      temperature: Math.floor(Math.random() * 10) + 95,
    };
    let covidMedicalInfo: CovidMedicalInfo = {
      testResult: randomEnum(TestResultType),
      vaccineCount: Math.floor(Math.random() * 3),
      covidCount: Math.floor(Math.random() * 3),
      lungCondition: Math.random() < 0.5,
    };
    return Math.random() < 0.5
      ? {
        type: MedicalInfoType.FLU,
        info: fluMedicalInfo
      }
      : {
        type: MedicalInfoType.COVID,
        info: covidMedicalInfo
      };
  }

  getPseudoPatientInfo(): PatientInfo {
    return {
      email: "In@aliquyam.com",
      sex: "femail",
      dateOfBirth: "2001-08-26",
      state: "Pennsylvania",
      city: "Pittsburgh",
      zipcode: "15213"
    };
  }
}