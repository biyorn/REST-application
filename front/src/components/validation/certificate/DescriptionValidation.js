export function verifyDescription(description) {
    return /^[A-Za-z0-9\s\.\,]{10,240}$/.test(description);
}