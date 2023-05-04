type LoginRequest = {
	username: string;
	password: string;
};

type LoginResponse = {
	token: string;
	refreshToken: string;
};

type RefreshRequest = {
	username: string;
	token: string;
};

type RefreshResponse = {
	token: string;
	refreshToken: string;
};

type RegisterRequest = {
	username: string;
	password: string;
};

type RegisterResponse = {};

interface IamApi {
	login(request: LoginRequest): Promise<LoginResponse | Error>;
	refresh(request: RefreshRequest): Promise<RefreshResponse | Error>;
	register(request: RegisterRequest): Promise<RegisterResponse | Error>;
}

export type {
	IamApi,
	LoginRequest,
	LoginResponse,
	RefreshRequest,
	RefreshResponse,
	RegisterRequest,
	RegisterResponse
};

class Test implements IamApi {
	fetch: (input: RequestInfo | URL, init?: RequestInit | undefined) => Promise<Response>;

	async login(request: LoginRequest): Promise<LoginResponse | Error> {
		try {
			const resp = await this.fetch('http://localhost:8080/api/iam');
			return {
				token: '',
				refreshToken: ''
			};
		} catch (e) {
			return this.handleError(e);
		}
	}

	async refresh(request: RefreshRequest): Promise<RefreshResponse | Error> {
		try {
			const resp = await this.fetch('/api/iam');
			return {
				token: '',
				refreshToken: ''
			};
		} catch (e) {
			return this.handleError(e);
		}
	}

	async register(request: RegisterRequest): Promise<RegisterResponse | Error> {
		try {
			const resp = await this.fetch('http://localhost:8080/api/iam');
			return {};
		} catch (e) {
			return this.handleError(e);
		}
	}

	private handleError(e: any): Error {
		if (e instanceof Error) {
			return e;
		}
		return new Error('unknown error');
	}

	constructor(
		fetch: (input: RequestInfo | URL, init?: RequestInit | undefined) => Promise<Response>
	) {
		this.fetch = fetch;
	}
}

export function factory(
	fetch: (input: RequestInfo | URL, init?: RequestInit | undefined) => Promise<Response>
) {
	return new Test(fetch);
}

// export default {
// 	login: async (request: LoginRequest) => {
// 		return {
// 			token: '',
// 			refreshToken: ''
// 		};
// 	},
// 	refresh: async (request: RefreshRequest) => {
// 		return {
// 			token: '',
// 			refreshToken: ''
// 		};
// 	},
// 	register: async (request: RegisterRequest) => {
// 		return {
// 			token: '',
// 			refreshToken: ''
// 		};
// 	}
// } satisfies IamApi;
