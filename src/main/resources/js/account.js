let index = {
    init: function() {
        $("#btn-save").on("click", () => {
            alert('클릭됨..');
            this.save();
        });
    },
    save: function (){
        alert('joinForm의 save함수 호출');
    }

}