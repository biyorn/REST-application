export function verifySurname(surname) {
    return /^[A-Z][a-z]{1,20}$/.test(surname);
}