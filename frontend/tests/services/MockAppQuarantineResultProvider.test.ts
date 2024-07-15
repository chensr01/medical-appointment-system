import { AppQuarantineResultProvider } from "../../src/services/contracts/ScheduleAppService";
import { MockAppQuarantineResultProvider } from "../../src/services/implementations/MockAppQuarantineResultProvider";
import { MedicalInfoType, TestResult, TestResultType } from "../../src/services/contracts/HealthAdminService";

const provider: AppQuarantineResultProvider = new MockAppQuarantineResultProvider();

const dummyTestResult: TestResult = {
  type: MedicalInfoType.FLU,
  medicalInfo: {
    testResult: TestResultType.UNKNOWN,
    daysSick: 3,
    haveFever: true,
    temperature: 101.2
  },
  patientInfo: {
    email: "huanmit@gmail.com",
    sex: "female",
    dateOfBirth: "2001-08-26",
    state: "Pennsylvania",
    city: "Pittsburgh",
    zipcode: "15213"
  }
}

describe('test getQuarantineResult', () => {
  it('should recommend quarantine on postive test result', async () => {
    const testResult: TestResult = {
      ...dummyTestResult,
      medicalInfo: {...dummyTestResult.medicalInfo, testResult: TestResultType.POSITIVE}
    };
    const result = await provider.getQuarantineResult(testResult);
    expect(result.duration).toBeGreaterThan(0);
  });

  for (const testResultType of [TestResultType.NEGATIVE, TestResultType.PENDING, TestResultType.UNKNOWN]) {
    it(`should recommend no quarantine on ${testResultType} test result`, async () => {
      const testResult: TestResult = {
        ...dummyTestResult,
        medicalInfo: {...dummyTestResult.medicalInfo, testResult: testResultType}
      };
      const result = await provider.getQuarantineResult(testResult);
      expect(result.duration).toBe(0);
    });
  }
});
