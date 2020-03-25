export function verifyDuration(duration) {
    return /^[1-9]{1,1}[0-9]{0,5}$/.test(duration);
}