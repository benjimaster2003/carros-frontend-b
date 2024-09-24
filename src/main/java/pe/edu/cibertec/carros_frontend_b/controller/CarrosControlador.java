package pe.edu.cibertec.carros_frontend_b.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import pe.edu.cibertec.carros_frontend_b.dto.CarrosRequestDto;
import pe.edu.cibertec.carros_frontend_b.dto.CarrosResponseDto;
import pe.edu.cibertec.carros_frontend_b.viewmodel.CarrosModel;

@Controller
@RequestMapping("/vehiculo")
public class CarrosControlador {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/buscarVehiculo")
    public String buscarVehiculo(Model model) {
        CarrosModel carrosModel = new CarrosModel(
                "00", "", "", "", "", "", "");
        model.addAttribute("carrosModel", carrosModel);
        return "buscar";
    }

    @PostMapping("/buscandoVehiculo")
    public String buscandoVehiculo(
            @RequestParam("placa") String placa,
            Model model) {
        if (placa == null || placa.trim().isEmpty()) {
            CarrosModel carrosModel = new CarrosModel(
                    "01", "Placa incorrecta.",
                    "", "", "", "", "");
            model.addAttribute("carrosModel", carrosModel);
            return "buscar";
        }

        try {
            String endpoint = "http://localhost:8081/vehiculos";
            CarrosRequestDto carrosRequestDto = new CarrosRequestDto(placa);
            CarrosResponseDto carrosResponseDto = restTemplate.postForObject(endpoint, carrosRequestDto, CarrosResponseDto.class);
            if (carrosResponseDto.codigo().equals("00")) {
                CarrosModel carrosModel = new CarrosModel(
                        "00", "", carrosResponseDto.autoMarca(), carrosResponseDto.autoModelo(), carrosResponseDto.autoNroAsientos(),
                        carrosResponseDto.autoPrecio(), carrosResponseDto.autoColor());
                model.addAttribute("carrosModel", carrosModel);
                return "detalles";
            } else {
                CarrosModel carrosModel = new CarrosModel(
                        "01", "Vehículo no encontrado.",
                        "", "", "", "", "");
                model.addAttribute("carrosModel", carrosModel);
                return "buscar";
            }
        } catch(Exception e) {
            CarrosModel carrosModel = new CarrosModel(
                    "99", "Error al buscar vehículo.",
                    "", "", "", "", "");
            model.addAttribute("carrosModel", carrosModel);
            System.out.println(e.getMessage());
            return "buscar";
        }
    }

}
