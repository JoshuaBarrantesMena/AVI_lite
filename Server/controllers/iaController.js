
const IA = require('../models/Ia');


exports.createRespuesta = async(req, res) => {
    const { valor, respuesta } = req.body;

    try {
        const nuevaRespuesta = await IA.create({ valor, respuesta });
        res.status(201).json({ message: "Respuesta de IA creada con éxito", iaId: nuevaRespuesta.id_ia });
    } catch (error) {
        console.error("Error al crear respuesta de IA:", error);
        res.status(500).json({ message: "Error al crear respuesta de IA", error });
    }
};


exports.getAllRespuestas = async(req, res) => {
    try {
        const respuestas = await IA.findAll();
        res.json(respuestas);
    } catch (error) {
        console.error("Error al obtener respuestas de IA:", error);
        res.status(500).json({ message: "Error al obtener respuestas de IA", error });
    }
};


exports.getRespuestaById = async(req, res) => {
    const { id } = req.params;
    if (id=='\n') {
        
        this.getAllRespuestas(req, res);  
        return;  
    }
    try {
        const respuesta = await IA.findOne({ where: { id_ia: id } });
        if (!respuesta) {
            return res.status(404).json({ message: "Respuesta de IA no encontrada" });
        }
        res.json(respuesta);
    } catch (error) {
        console.error("Error al obtener respuesta de IA:", error);
        res.status(500).json({ message: "Error al obtener respuesta de IA", error });
    }
};


exports.updateRespuesta = async(req, res) => {
    const { id } = req.params;
    const { valor, respuesta } = req.body;

    try {
        const ia = await IA.findOne({ where: { id_ia: id } });
        if (!ia) {
            return res.status(404).json({ message: "Respuesta de IA no encontrada" });
        }

        ia.valor = valor;
        ia.respuesta = respuesta;
        await ia.save();
        res.json({ message: "Respuesta de IA actualizada con éxito" });
    } catch (error) {
        console.error("Error al actualizar respuesta de IA:", error);
        res.status(500).json({ message: "Error al actualizar respuesta de IA", error });
    }
};


exports.deleteRespuesta = async(req, res) => {
    const { id } = req.params;

    try {
        const ia = await IA.findOne({ where: { id_ia: id } });
        if (!ia) {
            return res.status(404).json({ message: "Respuesta de IA no encontrada" });
        }

        await ia.destroy();
        res.json({ message: "Respuesta de IA eliminada con éxito" });
    } catch (error) {
        console.error("Error al eliminar respuesta de IA:", error);
        res.status(500).json({ message: "Error al eliminar respuesta de IA", error });
    }
};