import { PrismaClient } from '@prisma/client'

export const GET = async ({ cookies }) => {
    const client: PrismaClient = new PrismaClient();

    const user = await client.user.findUnique({
        where: {
            token: cookies.get("token")
        }
    });

    if (!user) {
        client.$disconnect();
        return new Response(
            JSON.stringify({ error: "User not found" }),
            {
                status: 404,
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
    }

    const programs = (await client.program.findMany({
        where: {
            userId: user.id
        }
    })).sort((a, b) => a.id - b.id);

    return new Response(
        JSON.stringify(programs),
        {
            status: 200,
            headers: {
                "Content-Type": "application/json"
            }
        }
    );
}

export const POST = async ({ cookies, request }) => {
    const client = new PrismaClient();
    const body = await request.json();

    if (!body.name) {
        client.$disconnect();
        return new Response(
            JSON.stringify({ error: "Name is required" }),
            {
                status: 400,
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
    }

    const user = await client.user.findUnique({
        where: {
            token: cookies.get("token")
        },
        select: {
            "Program": true,
            "id": true
        }
    });

    if (!user) {
        client.$disconnect();
        return new Response(
            JSON.stringify({ error: "User not found" }),
            {
                status: 404,
                headers: {
                    "Content-Type": "application/json"
                }
            }
        );
    }

    const program = await client.program.create({
        data: {
            name: body.name,
            User: {
                connect: {
                    id: user.id
                }
            }
        }
    });

    client.$disconnect();

    return new Response(
        JSON.stringify(program),
        {
            status: 200,
            headers: {
                "Content-Type": "application/json"
            }
        }
    );
}
