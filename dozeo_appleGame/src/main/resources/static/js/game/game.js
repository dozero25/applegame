window.onload = async () => {
    await GameSetService.getInstance().showInfoGameLogic();
    const compEvent = ComponentEvent.getInstance();
    compEvent.clickBtn();
    compEvent.startTimer();
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

class GameSetService {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new GameSetService();
        }
        return this.#instance;
    }

    async showInfoGameLogic() {
        const gameSet = await GameSetApi.getInstance().gameStart();

        if (!gameSet) return;

        const boardContainer = document.getElementById("gameBoard");

        boardContainer.innerHTML = "";

        const { rows, cols, board } = gameSet;

        let html = "";

        for (let i = 0; i < rows; i++) {
            for (let j = 0; j < cols; j++) {
                html += `<div class="cell apple" data-row="${i}" data-col="${j}">${board[i][j]}</div>`;
            }
        }

        boardContainer.innerHTML = html;

    }
}

class ComponentEvent {
    static #instance = null;

    static getInstance() {
        if (this.#instance == null) {
            this.#instance = new ComponentEvent();
        }
        return this.#instance;
    }

    constructor() {
        // DOM 요소 참조
        this.gameBoard = document.getElementById('gameBoard');
        this.scoreElement = document.getElementById('score');

        // 드레그 박스 요소 및 시작 좌표
        this.dragBox = null;
        this.startX = 0;
        this.startY = 0;

        // 점수 초기화
        this.score = 0;

        // 이벤트 핸들러 바인딩 (this context 유지)
        this.onMouseDown = this.onMouseDown.bind(this);
        this.onMouseMove = this.onMouseMove.bind(this);
        this.onMouseUp = this.onMouseUp.bind(this);

        // 마우스 이벤트 등록
        this.initEvents();
    }

    // 마우스 이벤트 등록
    initEvents() {
        this.gameBoard.addEventListener('mousedown', this.onMouseDown);
        document.addEventListener('mousemove', this.onMouseMove);
        document.addEventListener('mouseup', this.onMouseUp);
    }

    // 마우스 클릭 시작 시 실행
    onMouseDown(e) {
        if (e.button !== 0) return; // 좌클릭이 아닐 경우 무시

        this.clearSelection(); // 기존 선택 초기화

        // 드래그 시작 위치 저장
        this.startX = e.clientX;
        this.startY = e.clientY;

        this.dragBox = document.createElement('div');
        this.dragBox.className = 'drag-box';
        this.dragBox.style.position = 'absolute';
        this.dragBox.style.border = '1px solid #5D75F9';
        this.dragBox.style.backgroundColor = 'rgba(255, 255, 0, 0.2)';
        this.dragBox.style.pointerEvents = 'none';
        this.dragBox.style.zIndex = '999';

        this.gameBoard.appendChild(this.dragBox); // 보드에 추가
        this.updateDragBox(e); // 드래그 박스 크기 초기화
    }

    // 마우스 이동 시 실행
    onMouseMove(e) {
        if (!this.dragBox) return;

        this.updateDragBox(e); // 드래그 박스 실시간 갱신

        const boardRect = this.gameBoard.getBoundingClientRect();
        const dragRect = this.dragBox.getBoundingClientRect();

        // 드래그 박스의 상대 좌표 계산
        const boxRect = {
            left: dragRect.left - boardRect.left,
            top: dragRect.top - boardRect.top,
            right: dragRect.right - boardRect.left,
            bottom: dragRect.bottom - boardRect.top
        };

        // 드래그 범위 내 셀 강조 표시
        this.highlightSelectedCells(boxRect);

        // 선택된 셀들 점수 합 계산
        const selectedCells = this.gameBoard.querySelectorAll('.cell.apple.selected');
        let totalScore = 0;
        selectedCells.forEach(cell => {
            const value = parseInt(cell.textContent);
            if (!isNaN(value)) totalScore += value;
        });

        // 테두리 색 변경: 합이 10이면 빨간색, 아니면 노란색
        selectedCells.forEach(cell => {
            if (totalScore === 10) {
                cell.classList.add('red-border'); // 조건 충족 시 강조
            } else {
                cell.classList.remove('red-border'); // 조건 불충족 시 원상복구
            }
        });
    }

    // 마우스 클릭 해제 시 실행
    onMouseUp() {
        if (!this.dragBox) return;

        const selectedCells = this.gameBoard.querySelectorAll('.cell.apple.selected');

        let totalScore = 0;
        selectedCells.forEach(cell => {
            const value = parseInt(cell.textContent);
            if (!isNaN(value)) totalScore += value;
        });

        // 합이 10이면 사과 제거
        if (totalScore === 10) {
            selectedCells.forEach(cell => {
                cell.textContent = ''; // 숫자 제거
                cell.classList.remove('apple', 'selected'); // 클래스 제거
                cell.classList.add('empty'); // 빈 셀로 변경
                cell.dataset.score = '0'; // 해당 셀 점수 초기화
                cell.style.backgroundImage = 'none'; // 사과 이미지 제거
                cell.style.border = ''; // 테두리 제거
            });
            this.score += selectedCells.length; // 점수 증가
            this.updateScore();; // 점수 반영
        } else {
            // 선택 해제 및 테두리 제거
            selectedCells.forEach(cell => {
                cell.classList.remove('selected');
                cell.style.border = '';
            });
        }

        // 드래그 박스 제거
        this.dragBox.remove();
        this.dragBox = null;
    }

    // 드래그 박스의 위치와 크기를 마우스 위치에 따라 갱신
    updateDragBox(e) {
        const boardRect = this.gameBoard.getBoundingClientRect();

        const offsetX = Math.min(e.clientX, this.startX) - boardRect.left;
        const offsetY = Math.min(e.clientY, this.startY) - boardRect.top;
        const width = Math.abs(e.clientX - this.startX);
        const height = Math.abs(e.clientY - this.startY);

        this.dragBox.style.left = `${offsetX}px`;
        this.dragBox.style.top = `${offsetY}px`;
        this.dragBox.style.width = `${width}px`;
        this.dragBox.style.height = `${height}px`;
    }

    // 점수 UI 업데이트
    updateScore() {
        if (this.scoreElement) {
            this.scoreElement.textContent = this.score;
        }
    }

    // 선택된 셀 클래스 초기화
    clearSelection() {
        this.gameBoard.querySelectorAll('.cell.selected').forEach(cell => {
            cell.classList.remove('selected');
        });
    }

    // 드래그 박스 영역에 포함된 셀에 'selected' 클래스 추가
    highlightSelectedCells(boxRect) {
        this.clearSelection();

        // 게임 보드의 위치 및 크기 정보를 가져옴
        const boardRect = this.gameBoard.getBoundingClientRect();

        this.gameBoard.querySelectorAll('.cell.apple').forEach(cell => {
            const rect = cell.getBoundingClientRect();

            // 셀 위치를 보드 기준으로 상대 좌표 변환
            const relativeRect = {
                left: rect.left - boardRect.left,
                top: rect.top - boardRect.top,
                right: rect.right - boardRect.left,
                bottom: rect.bottom - boardRect.top
            };

            // 드레그 박스와 셀이 겹치는지 판별
            const isOverlapped =
                relativeRect.right > boxRect.left &&
                relativeRect.left < boxRect.right &&
                relativeRect.bottom > boxRect.top &&
                relativeRect.top < boxRect.bottom;

            // 겹치는 셀에 'selected' 클래스 추가(선택 상태 표시)
            if (isOverlapped) {
                cell.classList.add('selected');
            }
        });
    }

    clickBtn() {
        const compEvent = ComponentEvent.getInstance();

        document.getElementById('undoBtn').addEventListener('click', () => {
            compEvent.goHome();
        });

        document.getElementById('restartBtn').addEventListener('click', () => {
            compEvent.resetGame();
        });
    }

    goHome() {
        window.location.href = "/main";
    }

    resetGame() {
        window.location.href = "/game";
    }

    startTimer() {
        const totalTime = 60000;
        const bar = document.getElementById("timerBar");
        const startTime = Date.now();

        bar.style.transform = 'scaleX(1)';

        const modal = document.getElementById('gameModal');
        const finalScoreElement = document.getElementById('finalScore');

        function showGameOverModel() {
            modal.classList.remove('hidden');
        }

        const timerInterval = setInterval(() => {
            const elapsed = Date.now() - startTime;
            const timeLeft = totalTime - elapsed;
            const percent = Math.max(0, 1 - (elapsed / totalTime));
            bar.style.transform = `scaleX(${percent})`;


            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                showGameOverModel();

                if(finalScoreElement){
                    finalScoreElement.textContent = this.score;
                }
            }
        }, 50);

    }




}
