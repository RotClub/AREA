// @ts-expect-error The package jsonwebtoken is installed but is not recognized by linter.
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
	// @ts-expect-error The parameter 'err' is a complex data type and linter doesn't go well with any.
	await jwt.verify(token, process.env.JWT_SECRET, (err) => {
		if (err) {
			error(401, "Error during token validation: " + err);
		}
	});
	return true;
};

export const createJWTToken = async (payload: {
	email: string;
	password: string;
	role: UserRole;
}) => {
	const new_token = jwt.sign(payload, process.env.JWT_SECRET, { expiresIn: "1y" });
	// @ts-expect-error The parameter 'err' is a complex data type and linter doesn't go well with any.
	return await jwt.verify(new_token, process.env.JWT_SECRET, (err) => {
		return err
			? () => {
					console.error("Error verifying the JWT token:", err);
					return null;
				}
			: new_token;
	});
};
