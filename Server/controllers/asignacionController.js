// controllers/asignacionController.js
const Asignacion = require('../models/Asignacion');


exports.createAsignacion = async(req, res) => {
    const { titulo, id_profesor, id_curso, valor_porcentual, fecha_limite, descripcion } = req.body;

    try {
        const nuevaAsignacion = await Asignacion.create({ titulo, id_profesor, id_curso, valor_porcentual, fecha_limite, descripcion });
        res.status(201).json({ message: "Asignación creada con éxito", asignacionId: nuevaAsignacion.id_asignacion });
    } catch (error) {
        console.error("Error al crear asignación:", error);
        res.status(500).json({ message: "Error al crear asignación", error });
    }
};


exports.getAllAsignaciones = async(req, res) => {
    try {
        const asignaciones = await Asignacion.findAll();
        res.json(asignaciones);
    } catch (error) {
        console.error("Error al obtener asignaciones:", error);
        res.status(500).json({ message: "Error al obtener asignaciones", error });
    }
};

exports.getAllAsignacionesByCourse = async(req, res) => {

    const { id } = req.params;

    try {
        const asignaciones = await Asignacion.findAll({where: {id_curso: id}});
        res.json(asignaciones);
    } catch (error) {
        console.error("Error al obtener asignaciones:", error);
        res.status(500).json({ message: "Error al obtener asignaciones", error });
    }
};



exports.getAsignacionById = async(req, res) => {
    const { id } = req.params;
    if (id=='\n') {
        
        this.getAllAsignaciones(req, res);  
        return;  
    }
    try {
        const asignacion = await Asignacion.findOne({ where: { id_asignacion: id } });
        if (!asignacion) {
            return res.status(404).json({ message: "Asignación no encontrada" });
        }
        res.json(asignacion);
    } catch (error) {
        console.error("Error al obtener asignación:", error);
        res.status(500).json({ message: "Error al obtener asignación", error });
    }
};


exports.updateAsignacion = async(req, res) => {
    const { id } = req.params;
    const { titulo, id_profesor, id_curso, valor_porcentual, fecha_limite, descripcion } = req.body;

    try {
        const asignacion = await Asignacion.findOne({ where: { id_asignacion: id } });
        if (!asignacion) {
            return res.status(404).json({ message: "Asignación no encontrada" });
        }

        asignacion.titulo = titulo;
        asignacion.id_profesor = id_profesor;
        asignacion.id_curso = id_curso;
        asignacion.valor_porcentual = valor_porcentual;
        asignacion.fecha_limite = fecha_limite;
        asignacion.descripcion = descripcion;
        await asignacion.save();
        res.json({ message: "Asignación actualizada con éxito" });
    } catch (error) {
        console.error("Error al actualizar asignación:", error);
        res.status(500).json({ message: "Error al actualizar asignación", error });
    }
};


exports.deleteAsignacion = async(req, res) => {
    const { id } = req.params;

    try {
        const asignacion = await Asignacion.findOne({ where: { id_asignacion: id } });
        if (!asignacion) {
            return res.status(404).json({ message: "Asignación no encontrada" });
        }

        await asignacion.destroy();
        res.json({ message: "Asignación eliminada con éxito" });
    } catch (error) {
        console.error("Error al eliminar asignación:", error);
        res.status(500).json({ message: "Error al eliminar asignación", error });
    }
};