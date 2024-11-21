
const career = require('../models/Carrera');
const Departamento = require('../models/Departamentos');
const Carrera = require('../models/Carrera');
const Facultad = require('../models/Facultad');
const { Op } = require('sequelize');

exports.createCarrera = async(req, res) => {
    const { departamento_id, nombre } = req.body;

    try {
        const nuevaCarrera = await career.create({ departamento_id, nombre });
        res.status(201).json({ message: "Carrera creada con éxito", carreraId: nuevaCarrera.carrera_id });
    } catch (error) {
        console.error("Error al crear carrera:", error);
        res.status(500).json({ message: "Error al crear carrera", error });
    }
};


exports.getAllCarreras = async(req, res) => {
    try {
        const carreras = await career.findAll();
        res.json(carreras);
    } catch (error) {
        console.error("Error al obtener carreras:", error);
        res.status(500).json({ message: "Error al obtener carreras", error });
    }
};
exports.getCareerByUniversity= async (req, res) => {
    const { id } = req.params;

    try {
    
        const facultades = await Facultad.findAll({
            where: { universidad_id: id}
        });

        if (facultades.length === 0) {
            return res.status(404).json({ message: "No se encontraron facultades para esta universidad" });
        }

      
        const departamentos = await Departamento.findAll({
            where: {
                facultad_id: {
                    [Op.in]: facultades.map(facultad => facultad.facultad_id)
                }
            }
        });

        if (departamentos.length === 0) {
            return res.status(404).json({ message: "No se encontraron departamentos para estas facultades" });
        }

       
        const carreras = await Carrera.findAll({
            where: {
                departamento_id: {
                    [Op.in]: departamentos.map(departamento => departamento.departamento_id)
                }
            }
        });

        if (carreras.length === 0) {
            return res.status(404).json({ message: "No se encontraron carreras para estos departamentos" });
        }

     

 
        res.json(carreras);

    } catch (error) {
        console.error("Error al obtener cursos por universidad:", error);
        res.status(500).json({ message: "Error al obtener cursos por universidad", error });
    }
};




exports.getCareerByDepartment = async (req, res) => {
    const { departamento_id } = req.params;

    try {
        const carreras = await career.findAll({ where: { departamento_id } });
        
        if (!carreras.length) {
            return res.status(404).json({ message: "No se encontraron carreras para el departamento especificado" });
        }
        
        res.json(carreras);
    } catch (error) {
        console.error("Error al obtener carreras por departamento:", error);
        res.status(500).json({ message: "Error al obtener carreras", error });
    }
};


exports.getCarreraById = async(req, res) => {
    const { id } = req.params;
    if (id=='\n') {
        
        this.getAllCarreras(req, res);  
        return;  
    }
    try {
        const carrera = await career.findOne({ where: { carrera_id: id } });
        if (!carrera) {
            return res.status(404).json({ message: "Carrera no encontrada" });
        }
        res.json(carrera);
    } catch (error) {
        console.error("Error al obtener carrera:", error);
        res.status(500).json({ message: "Error al obtener carrera", error });
    }
};


exports.updateCarrera = async(req, res) => {
    const { id } = req.params;
    const { departamento_id, nombre } = req.body;

    try {
        const carrera = await career.findOne({ where: { carrera_id: id } });
        if (!carrera) {
            return res.status(404).json({ message: "Carrera no encontrada" });
        }

        carrera.departamento_id = departamento_id;
        carrera.nombre = nombre;
        await carrera.save();
        res.json({ message: "Carrera actualizada con éxito" });
    } catch (error) {
        console.error("Error al actualizar carrera:", error);
        res.status(500).json({ message: "Error al actualizar carrera", error });
    }
};


exports.deleteCarrera = async(req, res) => {
    const { id } = req.params;

    try {
        const carrera = await career.findOne({ where: { carrera_id: id } });
        if (!carrera) {
            return res.status(404).json({ message: "Carrera no encontrada" });
        }

        await carrera.destroy();
        res.json({ message: "Carrera eliminada con éxito" });
    } catch (error) {
        console.error("Error al eliminar carrera:", error);
        res.status(500).json({ message: "Error al eliminar carrera", error });
    }
};