window.onload = () => {

}


class MypageApi{
    static #instance = null;

    static getInstacne(){
        if(this.#instance == null){
            this.#instance = new MypageApi();
        }
        return this.#instance;
    }

    
}