    package model;

    public class VagaVIPModel extends VagaModel {

        protected static double taxa = 1.2;

        public VagaVIPModel(String id) {
            super(id);
        }

        @Override
        public String getTipo() {
            return "VIP";
        }


        @Override
        public double calcularPreco(int minutos) {
            return Math.min((minutos / 15) * precoPorFacao * taxa, limite);
        }
    }
