export class Login {
  constructor(
    public username: string,
    public password: string,
    public rememberMe: boolean,
    recaptchaToken?: string, // <--- ajoute cette ligne si elle n'existe pas
  ) {}
}
