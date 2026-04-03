export class OperationResult {
  errors: string[] = [];
  warnings: string[] = [];
  successMessage: string = '';
  successHandler: () => void = () => {};

  setErrors(errors: string[]): OperationResult {
    this.errors = [...errors];
    return this;
  }

  setWarnings(warnings: string[]): OperationResult {
    this.warnings = [...warnings];
    return this;
  }

  setSuccessMessage(message: string): OperationResult {
    this.successMessage = message;
    return this;
  }

  setSuccessHandler(handler: () => void): OperationResult {
    this.successHandler = handler;
    return this;
  }
}
