export function getToken() {
    if (process.env.VERCEL_URL) {
        return "_vercel_jwt";
    } else {
        return "token";
    }
} 