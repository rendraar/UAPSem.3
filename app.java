package com.example.uap;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class app extends Application {

    private TableView<DataKeuangan> table = new TableView<>();
    private final ObservableList<DataKeuangan> data = FXCollections.observableArrayList();

    private TextField gajiField;
    private TextField pengeluaranField;
    private Label resultLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Manajemen Keuangan");

        // Inisialisasi UI JavaFX
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        gajiField = new TextField();
        gajiField.setPromptText("Masukkan Gaji Bulanan Anda");

        pengeluaranField = new TextField();
        pengeluaranField.setPromptText("Masukkan Pengeluaran Bulanan Anda");

        Button hitungButton = new Button("Hitung");
        hitungButton.setOnAction(e -> {
            try {
                cekValid(gajiField.getText(), pengeluaranField.getText());
                hitungKehematan();
            } catch (Exception ex) {
                showAlert("Error", "Input tidak valid", ex.getMessage());
            }
        });

        resultLabel = new Label();

        // Tabel untuk menampilkan data keuangan
        TableColumn<DataKeuangan, Double> gajiColumn = new TableColumn<>("Gaji");
        gajiColumn.setCellValueFactory(cellData -> cellData.getValue().gajiProperty().asObject());

        TableColumn<DataKeuangan, Double> pengeluaranColumn = new TableColumn<>("Pengeluaran");
        pengeluaranColumn.setCellValueFactory(cellData -> cellData.getValue().pengeluaranProperty().asObject());

        TableColumn<DataKeuangan, Double> kebutuhanColumn = new TableColumn<>("Kebutuhan");
        kebutuhanColumn.setCellValueFactory(cellData -> cellData.getValue().kebutuhanProperty().asObject());

        TableColumn<DataKeuangan, Double> tabunganColumn = new TableColumn<>("Tabungan");
        tabunganColumn.setCellValueFactory(cellData -> cellData.getValue().tabunganProperty().asObject());

        TableColumn<DataKeuangan, Double> jajanColumn = new TableColumn<>("Jajan");
        jajanColumn.setCellValueFactory(cellData -> cellData.getValue().jajanProperty().asObject());

        TableColumn<DataKeuangan, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setPrefWidth(140);
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(gajiColumn, pengeluaranColumn, kebutuhanColumn, tabunganColumn, jajanColumn, statusColumn);
        table.setItems(data);

        Button tambahButton = new Button("Tambah");
        tambahButton.setOnAction(e -> {
            try {
                cekValid(gajiField.getText(), pengeluaranField.getText());
                tambahData();
            } catch (Exception ex) {
                showAlert("Error", "Input tidak valid", ex.getMessage());
            }
        });

        Button hapusButton = new Button("Hapus");
        hapusButton.setOnAction(e -> hapusData());

        root.getChildren().addAll(gajiField, pengeluaranField, hitungButton, resultLabel, table, tambahButton, hapusButton);

        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void hitungKehematan() {
        int gaji = Integer.parseInt(gajiField.getText());
        int pengeluaran = Integer.parseInt(pengeluaranField.getText());

        if (gaji * 0.7 < pengeluaran) {
            resultLabel.setText("Anda Terlalu Boros");
        } else {
            resultLabel.setText("Anda Sudah Berhemat");
        }
    }

    private void tambahData() {
        double gaji = Integer.parseInt(gajiField.getText());
        double pengeluaran = Integer.parseInt(pengeluaranField.getText());
        String status = resultLabel.getText();
        double kebutuhan = gaji * 0.5;
        double tabungan = gaji * 0.2;
        double jajan = gaji * 0.3;

        data.add(new DataKeuangan(gaji, pengeluaran, kebutuhan, tabungan, jajan, status));
    }

    private void hapusData() {
        DataKeuangan selectedData = table.getSelectionModel().getSelectedItem();
        if (selectedData != null) {
            data.remove(selectedData);
        }
    }

    private void cekValid(String gaji, String pengeluaran) throws Exception {
        if (gaji.isEmpty() || pengeluaran.isEmpty()) {
            throw new Exception("Field tidak boleh kosong");
        }
        if (Integer.parseInt(gaji) < 1000 || Integer.parseInt(gaji) > 9999999) {
            throw new Exception("Inputan harus diantara 1000 dan 9999999");
        } else if (Integer.parseInt(pengeluaran) < 1000 || Integer.parseInt(pengeluaran) > 9999999) {
            throw new Exception("Inputan harus diantara 1000 dan 9999999");
        }

    }

    private void showAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static class DataKeuangan {
        private final SimpleDoubleProperty gaji;
        private final SimpleDoubleProperty pengeluaran;
        private final SimpleDoubleProperty kebutuhan;
        private final SimpleDoubleProperty tabungan;
        private final SimpleDoubleProperty jajan;
        private final StringProperty status;

        private DataKeuangan(double gaji, double pengeluaran, double kebutuhan, double tabungan, double jajan, String status) {
            this.gaji = new SimpleDoubleProperty(gaji);
            this.pengeluaran = new SimpleDoubleProperty(pengeluaran);
            this.kebutuhan = new SimpleDoubleProperty (kebutuhan);
            this.tabungan = new SimpleDoubleProperty (tabungan);
            this.jajan = new SimpleDoubleProperty (jajan);
            this.status = new SimpleStringProperty (status);
        }

        public double getGaji() {
            return gaji.get();
        }

        public void setGaji(double gaji) {
            this.gaji.set(gaji);
        }

        public SimpleDoubleProperty gajiProperty() {
            return gaji;
        }

        public double getPengeluaran() {
            return pengeluaran.get();
        }

        public void setPengeluaran(double pengeluaran) {
            this.pengeluaran.set(pengeluaran);
        }

        public SimpleDoubleProperty pengeluaranProperty() {
            return pengeluaran;
        }

        public String getStatus() {
            return status.get();
        }


        public StringProperty statusProperty() {
            return status;
        }

        public double getKebutuhan() {
            return kebutuhan.get();
        }

        public SimpleDoubleProperty kebutuhanProperty() {
            return kebutuhan;
        }

        public double getTabungan() {
            return tabungan.get();
        }

        public SimpleDoubleProperty tabunganProperty() {
            return tabungan;
        }

        public double getJajan() {
            return jajan.get();
        }

        public SimpleDoubleProperty jajanProperty() {
            return jajan;
        }

    }
}