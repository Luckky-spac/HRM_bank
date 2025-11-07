<script lang="ts" setup>
import AppFooter from '@/components/AppFooter.vue';
import AppNavbar from '@/components/AppNavbar.vue';

const logoUrl = 'https://www.bcel.com.la/bcel/resources/imgs/logobcel.png';
</script>

<template>
  <AppNavbar />

  <!-- Hero Section -->
  <section class="bg-gradient-to-b from-red-900 to-red-700 text-white py-20">
    <div class="container mx-auto px-6 text-center">
      <img :src="logoUrl" alt="BCEL Logo" class="h-24 mx-auto mb-6" />
      <h1 class="text-4xl md:text-5xl font-bold mb-4">
        ທະນາຄານການຄ້າຕ່າງປະເທດລາວ ມະຫາຊົນ
      </h1>
      <p class="text-xl opacity-90">
        Banque pour le Commerce Exterieur Lao Public (BCEL)
      </p>
      <p class="mt-4 text-lg max-w-3xl mx-auto">
        ທະນາຄານທີ່ເຊື່ອຖືໄດ້ ພ້ອມບໍລິການທາງການເງິນທີ່ທັນສະໄໝ ເພື່ອຄວາມສະດວກສະບາຍຂອງທ່ານ
      </p>
    </div>
  </section>

  <!-- About Content -->
  <section class="py-16 bg-gray-50">
    <div class="container mx-auto px-6 max-w-5xl">
      <div class="grid md:grid-cols-3 gap-10 items-center mb-12">
        <div class="text-center">
          <div class="bg-red-600 text-white rounded-full w-20 h-20 flex items-center justify-center mx-auto mb-4 text-3xl font-bold">
            50
          </div>
          <p class="text-lg font-semibold">ປີແຫ່ງການບໍລິການ</p>
        </div>
        <div class="text-center">
          <div class="bg-yellow-500 text-white rounded-full w-20 h-20 flex items-center justify-center mx-auto mb-4 text-3xl font-bold">
            1
          </div>
          <p class="text-lg font-semibold">ທະນາຄານທຳອິດທີ່ຈົດທະບຽນໃນຕະຫຼາດຫຼັກຊັບ</p>
        </div>
        <div class="text-center">
          <div class="bg-green-600 text-white rounded-full w-20 h-20 flex items-center justify-center mx-auto mb-4 text-3xl font-bold">
            100+
          </div>
          <p class="text-lg font-semibold">ສາຂາທົ່ວປະເທດ</p>
        </div>
      </div>

      <div class="bg-white rounded-xl shadow-lg p-10 border border-gray-200">
        <h2 class="text-3xl font-bold text-red-900 mb-8 text-center">
          ປະຫວັດຄວາມເປັນມາ
        </h2>

        <div class="space-y-8 text-lg text-gray-700 leading-relaxed font-laos">
          <p>
            ທະນາຄານການຄ້າຕ່າງປະເທດລາວ (ທຄຕລ) ແມ່ນໄດ້ສ້າງຕັ້ງຂຶ້ນໃນໄລຍະທີ່ ສປປ ລາວໄດ້ປະກາດ ສະຖາປະນາ ວັນທີ 2 ທັນວາ 1975 ແລະ ໄດ້ດຳເນີນທຸລະກິດ ນັບແຕ່ປີ 1975 ເປັນຕົ້ນມາ. ໃນໄລຍະປີ 1975 ຫາ 1976 ທຄຕລ ໄດ້ປະຕິບັດພາລະບົດບາດ ພິເສດເຊິ່ງເປັນສາຂາທະນາຄານແຫ່ງລັດ ໃນເມື່ອກ່ອນ ທະນາຄານແຫ່ງ ສປປ ລາວ ໃນປັດຈຸບັນ ແລະ ໄດ້ຖືກມອບໝາຍ ໃຫ້ເປັນທະນາຄານແຫ່ງດຽວໃນ ສປປ ລາວ ທີ່ສາມາດດຳເນີນທຸລະກິດກັບສາກົນ (Inter Banking) ແລະ ກໍຍັງໄດ້ຖືກມອບໝາຍໃຫ້ດຳເນີນການຄຸ້ມຄອງບັນດາເງິນຊ່ວຍເຫຼືອ ແລະ ເງິນຕ່າງໆຈາກຕ່າງປະເທດ ແລະ ອົງການຈັດຕັ້ງສາກົນທີ່ມອບໃຫ້ລັດຖະບານ ສປປ ລາວ
          </p>

          <div class="bg-red-50 border-l-4 border-red-600 p-6 rounded-r-lg">
            <p class="font-semibold text-red-900">
              ນັບຕັ້ງແຕ່ ວັນທີ 01 ເດືອນ ພະຈິກ 1989
            </p>
            <p class="mt-2">
              ທະນາຄານການຄ້າຕ່າງປະເທດລາວ ແມ່ນໄດ້ຖືກປ່ຽນເປັນ ທະນາຄານທຸລະກິດລັດເຕັມຮູບແບບ ໂດຍອີງຕາມກົດໝາຍທະນາຄານແຫ່ງ ສປປ ລາວ ແລະ ດຳລັດກ່ຽວກັບການ ຄຸ້ມຄອງທະນາຄານທຸລະກິດ. ໂດຍສະເພາະການຫັນປ່ຽນທະນາຄານ ຈາກລັດຄຸ້ມຄອງບໍລິຫານ ໄປສູ່ຮູບແບບ ທະນາຄານທຸລະກິດລັດ ທີ່ມີການໄລ່ລຽງປະສິດທິຜົນກຸ້ມຕົນເອງທາງດ້ານລາຍຮັບ-ລາຍຈ່າຍ ແລະ ງົບປະມານເງິນເດືອນຂອງພະນັກງານ.
            </p>
          </div>

          <p>
            ພ້ອມດຽວກັນນັ້ນ ໃນວັນທີ່ 11 ມັງກອນ 2011 ທຄຕລ ກໍໄດ້ຫັນປຽນຈາກ ທະນາຄານ ທຸລະກິດລັດ ມາເປັນ<strong class="text-red-700"> ທະນາຄານການຄ້າຕ່າງປະເທດລາວ ມະຫາຊົນ</strong> ແລະ ຈົດທະບຽນຢູ່ໃນຕະຫຼາດຫຼັກຊັບລາວ ເປັນທະນາຄານທຳອິດໃນ ສປປ ລາວ
          </p>
        </div>

        <div class="mt-12 text-center">
          <router-link to="/services" class="bg-red-600 hover:bg-red-700 text-white font-bold py-4 px-10 rounded-full inline-block transition">
            ສຳຫຼວດບໍລິການຂອງພວກເຮົາ
          </router-link>
        </div>
      </div>
    </div>
  </section>

  <!-- Trust Section -->
  <section class="bg-red-900 text-white py-16">
    <div class="container mx-auto px-6 text-center">
      <h2 class="text-3xl font-bold mb-8">ເປັນທີ່ເຊື່ອໝັ້ນຂອງປະຊາຊົນລາວ</h2>
      <div class="grid md:grid-cols-4 gap-8 text-sm">
        <div>
          <p class="text-4xl font-bold text-yellow-400">50+</p>
          <p class="mt-2">ປີແຫ່ງປະສົບການ</p>
        </div>
        <div>
          <p class="text-4xl font-bold text-yellow-400">3M+</p>
          <p class="mt-2">ລູກຄ້າທົ່ວປະເທດ</p>
        </div>
        <div>
          <p class="text-4xl font-bold text-yellow-400">100+</p>
          <p class="mt-2">ສາຂາ ແລະ ຈຸດບໍລິການ</p>
        </div>
        <div>
          <p class="text-4xl font-bold text-yellow-400">24/7</p>
          <p class="mt-2">BCEL One ບໍລິການທາງອິນເຕີເນັດ</p>
        </div>
      </div>
    </div>
  </section>

  <AppFooter />
</template>

<style scoped>
.font-laos {
  font-family: 'Phetsarath OT', 'Noto Sans Lao', sans-serif;
  line-height: 2;
}

@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+Lao:wght@400;500;700&display=swap');

* {
  font-family: 'Noto Sans Lao', 'Phetsarath OT', sans-serif;
}
</style>
