/** 금액 100단위 끊기 */
export default {

    getNumberFormatted(val){
        return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }


}