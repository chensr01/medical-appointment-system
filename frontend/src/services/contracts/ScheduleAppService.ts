import { QuarantineResult } from "./PolicyMakerService";
import { TestResult } from "./HealthAdminService";

export interface AppQuarantineResultProvider {
  getQuarantineResult(testResult: TestResult): Promise<QuarantineResult>;
}