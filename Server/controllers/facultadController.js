
const Facultad = require('../models/Facultad');
const Departamento = require('../models/Departamentos');
const Carrera = require('../models/Carrera');
const { Op } = require('sequelize');







exports.getCareersByUniversity= async (req, res) => {
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




exports.createFacultad = async(req, res) => {
    const { universidad_id, nombre } = req.body;

    try {
        const nuevaFacultad = await Facultad.create({ universidad_id, nombre });
        res.status(201).json({ message: "Facultad creada con éxito", facultadId: nuevaFacultad.facultad_id });
    } catch (error) {
        console.error("Error al crear facultad:", error);
        res.status(500).json({ message: "Error al crear facultad", error });
    }
};


exports.getAllFacultades = async (req, res) => {
    try {
      
      const facultades = await Facultad.findAll();
      res.json(facultades);
    } catch (error) {
      console.error("Error al obtener facultades:", error);
      res.status(500).json({ message: "Error al obtener facultades", error });
    }
  };
  // controllers/facultadController.js
exports.getFacultadesByUniversity = async (req, res) => {
    const { universidad_id } = req.query; 

    if (!universidad_id) {
        return res.status(400).json({ message: "Falta el parámetro universidad_id" });
    }

    try {
        const facultades = await Facultad.findAll({
            where: { universidad_id } 
        });
        
        if (facultades.length === 0) {
            return res.status(404).json({ message: "No se encontraron facultades para esta universidad" });
        }

        res.json(facultades); // Enviar los resultados
        console.log(facultades);
    } catch (error) {
        console.error("Error al obtener facultades:", error);
        res.status(500).json({ message: "Error al obtener facultades", error });
    }
};



exports.getFacultadById = async(req, res) => {
    const { id } = req.params;
    if (id=='\n') {
        
        this.getAllFacultades(req, res);  
        return;  
    }
    try {
        const facultad = await Facultad.findOne({ where: { facultad_id: id } });
        if (!facultad) {
            return res.status(404).json({ message: "Facultad no encontrada" });
        }
        res.json(facultad);
    } catch (error) {
        console.error("Error al obtener facultad:", error);
        res.status(500).json({ message: "Error al obtener facultad", error });
    }
};


exports.updateFacultad = async(req, res) => {
    const { id } = req.params;
    const { universidad_id, nombre } = req.body;

    try {
        const facultad = await Facultad.findOne({ where: { facultad_id: id } });
        if (!facultad) {
            return res.status(404).json({ message: "Facultad no encontrada" });
        }

        facultad.universidad_id = universidad_id;
        facultad.nombre = nombre;
        await facultad.save(); // Guardar cambios
        res.json({ message: "Facultad actualizada con éxito" });
    } catch (error) {
        console.error("Error al actualizar facultad:", error);
        res.status(500).json({ message: "Error al actualizar facultad", error });
    }
};


exports.deleteFacultad = async(req, res) => {
    const { id } = req.params;

    try {
        const facultad = await Facultad.findOne({ where: { facultad_id: id } });
        if (!facultad) {
            return res.status(404).json({ message: "Facultad no encontrada" });
        }

        await facultad.destroy(); // Eliminar la facultad
        res.json({ message: "Facultad eliminada con éxito" });
    } catch (error) {
        console.error("Error al eliminar facultad:", error);
        res.status(500).json({ message: "Error al eliminar facultad", error });
    }
};