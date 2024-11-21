
const express = require('express');
const universidadController = require('../controllers/universityController');
const router = express.Router();
const authMiddleware = require('../middlewares/authMiddleware'); 



router.post('/', authMiddleware, universidadController.createUniversidad);

router.get('/', universidadController.getAllUniversidades);

router.get('/:id', authMiddleware, universidadController.getUniversidadById);

router.put('/:id', authMiddleware, universidadController.updateUniversidad);

router.delete('/:id', authMiddleware, universidadController.deleteUniversidad);

module.exports = router;

