window.onload = () => {
    GameSetService.getInstance().showInfoGameLogic();
}


class GameSetApi {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new GameSetApi();
        }
        return this.#instance;
    }

    async gameStart() {
        try {
            const response = await fetch('http://localhost:8000/api/set/start-game', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
            });

            if (!response.ok) throw new Error('Network response was not ok');

            const result = await response.json();
            return result.data;

        } catch (error) {
            console.error('게임 시작 중 오류:', error);
            return null;
        }
    }

}

class GameSetService{
    static #instance = null;

    static getInstance(){
        if(this.#instance == null){
            this.#instance = new GameSetService();
        }
        return this.#instance;
    }

    async showInfoGameLogic(){
        const gameSet = await GameSetApi.getInstance().gameStart();

        if(!gameSet) return;

        const boardContainer = document.getElementById("gameBoard");

        boardContainer.innerHTML = "";

        const {rows, cols, board} = gameSet;

        let html = "";

        for(let i = 0; i < rows; i++){
            for(let j = 0; j < cols; j++){
                html += `<div class="cell" data-row="${i}">${board[i][j]}</div>`
            }
        }

        boardContainer.innerHTML = html;
        ComponentEvent.getInstance().playCuteBGM();
    }
}

class ComponentEvent{
    static #instance = null;

    static getInstance(){
        if(this.#instance == null){
            this.#instance = new ComponentEvent();
        }
        return this.#instance;
    }

}