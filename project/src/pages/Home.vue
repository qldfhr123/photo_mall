

<template>
 <div class = "home">

    <div class="album py-5 bg-body-tertiary">
      <div class="container">
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
          <!-- vue 문법 띄어쓰기 조심히 하기  변수명 맞춰주기-->
          <div class="col" v-for="(items, idx) in state.items" :key="idx">
            <Card :item="items"/>
          </div>
       
        </div>
      </div>
    </div>
</div>


</template>


<script>
import Card from '@/components/Card.vue';
import axios from 'axios';
import { reactive } from 'vue';

export default {
    name: "Home",
    components: {Card},
    setup() {
      // 반응형 상태 생성 함수
      const state = reactive({
        items: []
      })

      axios.get("/api/items").then(({data})=> {
        // 스프링에서 데이터를 받아서 state에 저장
        state.items = data;
      });

      return {state}
    }
}

</script>

<style scoped>
</style>