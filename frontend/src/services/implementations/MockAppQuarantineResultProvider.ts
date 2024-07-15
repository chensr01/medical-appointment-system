import { PatientAndCovidInfo, PatientAndFluInfo, PatientAndTestInfo, QuarantineResult, QuarantineResultProvider } from "../contracts/PolicyMakerService";
import { TestResult, TestResultType, MedicalInfoType, CovidMedicalInfo, FluMedicalInfo } from "../contracts/HealthAdminService";
import { AppQuarantineResultProvider } from "../contracts/ScheduleAppService";
import { RandomQuarantineResultProvider } from "./RandomQuarantineResultProvider";

const NO_QUARANTINE: QuarantineResult = {
  duration: 0,
}

export class MockAppQuarantineResultProvider implements AppQuarantineResultProvider {
  private randomQuarantineResultProvider: QuarantineResultProvider;

  constructor() {
    this.randomQuarantineResultProvider = new RandomQuarantineResultProvider();
  }

  async getQuarantineResult(testResult: TestResult): Promise<QuarantineResult> {
    // If the test result is positive, query the policymaker service for the quarantine result (randomly generated)
    if (testResult.medicalInfo.testResult === TestResultType.POSITIVE) {

      // Covid
      if (testResult.type === MedicalInfoType.COVID) {
        const covidMedicalInfo = testResult.medicalInfo as CovidMedicalInfo;
        let patientAndTestInfo : PatientAndCovidInfo = {
          apptType: testResult.type,
          city: testResult.patientInfo.city,
          state: testResult.patientInfo.state,
          zipcode: testResult.patientInfo.zipcode,
          dob: testResult.patientInfo.dateOfBirth,
          gender: testResult.patientInfo.sex,
          vaccinationCount: covidMedicalInfo.vaccineCount,
          diseaseCount: covidMedicalInfo.covidCount,
          lungCondition: covidMedicalInfo.lungCondition
        }
        return this.randomQuarantineResultProvider.getQuarantineResult(patientAndTestInfo);

      // Flu
      } else if (testResult.type === MedicalInfoType.FLU) {
        const fluMedicalInfo = testResult.medicalInfo as FluMedicalInfo;
        let patientAndTestInfo : PatientAndFluInfo = {
          apptType: testResult.type,
          city: testResult.patientInfo.city,
          state: testResult.patientInfo.state,
          zipcode: testResult.patientInfo.zipcode,
          dob: testResult.patientInfo.dateOfBirth,
          gender: testResult.patientInfo.sex,
          daysSick: fluMedicalInfo.daysSick,
          haveFever: fluMedicalInfo.haveFever,
          temperature: fluMedicalInfo.temperature
        }
        return this.randomQuarantineResultProvider.getQuarantineResult(patientAndTestInfo);

      } else {
        return NO_QUARANTINE;
      }

    }

    // Else, recommend no quarantine
    return NO_QUARANTINE;
    
  }
}