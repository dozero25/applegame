class PrincipalApi {
    static #instance = null;
    static getInstance() {
        if(this.#instance == null) {
            this.#instance = new PrincipalApi();
        }
        return this.#instance;
    }

    async getPrincipal() {
        try {
            const response = await fetch(`http://localhost:8000/api/account/principal`)
            const result = await response.json();

            return result.data;
        } catch (error) {
            console.error("getPrincipal", error);
            return error;
        }
    }
}