import { PatientAndTestInfo, QuarantineResult, QuarantineResultProvider } from "../contracts/PolicyMakerService";

export class RandomQuarantineResultProvider implements QuarantineResultProvider {
  // Generate a random quarantine result
  async getQuarantineResult(patientAndTestInfo : PatientAndTestInfo): Promise<QuarantineResult> {
    return {
      duration: Math.floor(Math.random() * 14) + 1,
    }
  }
}