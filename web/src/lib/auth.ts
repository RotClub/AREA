import jwt from "jsonwebtoken";
import { UserRole } from "@prisma/client";
import { error } from "@sveltejs/kit";
import { createHash } from "node:crypto";

export const encryptPWD = (password: string | null) => {
	const hash = createHash("sha1");
	const salt = !process.env.JWT_SECRET ? "" : process.env.JWT_SECRET;
	if (!password) {
		hash.update(salt);
	} else {
		hash.update(password + salt);
	}
	return hash.digest("hex");
};

export const verifyJWTAuth = async (token: string) => {
	if (!token) {
		error(401, "Not logged in");
	}
	try {
		jwt.verify(token, process.env.JWT_SECRET || "");
	} catch (err) {
		console.log("Error during token validation: " + err);
		return false;
	}
	return true;
};

export const createJWTToken = (payload: {
	email: string;
	password: string;
	username: string;
	role: UserRole;
	old_token?: string;
}) => {
	const new_token = jwt.sign(payload, process.env.JWT_SECRET || "", { expiresIn: "1y" });
	try {
		jwt.verify(new_token, process.env.JWT_SECRET || "");
		return new_token as string;
	} catch (err) {
		console.error("Error verifying the JWT token:", err);
		return null;
	}
};
