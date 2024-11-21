
const Universidad = require('../models/University');
const { get } = require('../routes/authRoutes');


exports.createUniversidad = async(req, res) => {
    const { nombre } = req.body;

    try {
        const nuevaUniversidad = await Universidad.create({ nombre });
        res.status(201).json({ message: "Universidad creada con éxito", universidadId: nuevaUniversidad.universidad_id });
    } catch (error) {
        console.error("Error al crear universidad:", error);
        res.status(500).json({ message: "Error al crear universidad", error });
    }
};


exports.getAllUniversidades = async(req, res) => {
    try {
        const universidades = await Universidad.findAll();
        res.json(universidades);
    } catch (error) {
        console.error("Error al obtener universidades:", error);
        res.status(500).json({ message: "Error al obtener universidades", error });
    }
};


exports.getUniversidadById = async(req, res) => {
    const { id } = req.params;

    if (id=='\n') {
        
        this.getAllUniversidades(req, res);  
        return;  
    }

    try {
        const universidad = await Universidad.findOne({ where: { universidad_id: id } });
        if (!universidad) {
            return res.status(404).json({ message: "Universidad no encontrada" });
        }
        res.json(universidad);
    } catch (error) {
        console.error("Error al obtener universidad:", error);
        res.status(500).json({ message: "Error al obtener universidad", error });
    }
};


exports.updateUniversidad = async(req, res) => {
    const { id } = req.params;
    const { nombre } = req.body;

    try {
        const universidad = await Universidad.findOne({ where: { universidad_id: id } });
        if (!universidad) {
            return res.status(404).json({ message: "Universidad no encontrada" });
        }

        universidad.nombre = nombre;
        await universidad.save();
        res.json({ message: "Universidad actualizada con éxito" });
    } catch (error) {
        console.error("Error al actualizar universidad:", error);
        res.status(500).json({ message: "Error al actualizar universidad", error });
    }
};


exports.deleteUniversidad = async(req, res) => {
    const { id } = req.params;

    try {
        const universidad = await Universidad.findOne({ where: { universidad_id: id } });
        if (!universidad) {
            return res.status(404).json({ message: "Universidad no encontrada" });
        }

        await universidad.destroy();
        res.json({ message: "Universidad eliminada con éxito" });
    } catch (error) {
        console.error("Error al eliminar universidad:", error);
        res.status(500).json({ message: "Error al eliminar universidad", error });
    }
};