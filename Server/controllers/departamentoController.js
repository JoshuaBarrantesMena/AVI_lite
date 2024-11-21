// controllers/departamentoController.js
const Departamento = require('../models/Departamentos');


const Carrera = require('../models/Carrera');
const Facultad = require('../models/Facultad');
const { Op } = require('sequelize');

exports.createDepartamento = async(req, res) => {
    const { facultad_id, nombre } = req.body;

    try {
        const nuevoDepartamento = await Departamento.create({ facultad_id, nombre });
        res.status(201).json({ message: "Departamento creado con éxito", departamentoId: nuevoDepartamento.departamento_id });
    } catch (error) {
        console.error("Error al crear departamento:", error);
        res.status(500).json({ message: "Error al crear departamento", error });
    }
};
exports.getByUniversity= async (req, res) => {
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

       
        

        res.json(departamentos);

    } catch (error) {
        console.error("Error al obtener cursos por universidad:", error);
        res.status(500).json({ message: "Error al obtener cursos por universidad", error });
    }
};

exports.getAllDepartamentos = async(req, res) => {
    try {
        const departamentos = await Departamento.findAll();
        res.json(departamentos);
    } catch (error) {
        console.error("Error al obtener departamentos:", error);
        res.status(500).json({ message: "Error al obtener departamentos", error });
    }
};


exports.getDepartamentoById = async(req, res) => {
    const { id } = req.params;
    if (id=='\n') {
        
        this.getAllDepartamentos(req, res);  
        return;  
    }
    try {
        const departamento = await Departamento.findOne({ where: { departamento_id: id } });
        if (!departamento) {
            return res.status(404).json({ message: "Departamento no encontrado" });
        }
        res.json(departamento);
    } catch (error) {
        console.error("Error al obtener departamento:", error);
        res.status(500).json({ message: "Error al obtener departamento", error });
    }
};

exports.updateDepartamento = async(req, res) => {
    const { id } = req.params;
    const { facultad_id, nombre } = req.body;

    try {
        const departamento = await Departamento.findOne({ where: { departamento_id: id } });
        if (!departamento) {
            return res.status(404).json({ message: "Departamento no encontrado" });
        }

        departamento.facultad_id = facultad_id;
        departamento.nombre = nombre;
        await departamento.save();
        res.json({ message: "Departamento actualizado con éxito" });
    } catch (error) {
        console.error("Error al actualizar departamento:", error);
        res.status(500).json({ message: "Error al actualizar departamento", error });
    }
};


exports.deleteDepartamento = async(req, res) => {
    const { id } = req.params;

    try {
        const departamento = await Departamento.findOne({ where: { departamento_id: id } });
        if (!departamento) {
            return res.status(404).json({ message: "Departamento no encontrado" });
        }

        await departamento.destroy();
        res.json({ message: "Departamento eliminado con éxito" });
    } catch (error) {
        console.error("Error al eliminar departamento:", error);
        res.status(500).json({ message: "Error al eliminar departamento", error });
    }
};