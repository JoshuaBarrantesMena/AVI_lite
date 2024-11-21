
const express = require('express');
const facultadController = require('../controllers/facultadController');
const authMiddleware = require('../middlewares/authMiddleware'); 


const router = express.Router();


router.post('/', authMiddleware, facultadController.createFacultad);
router.get('/getAll', authMiddleware, facultadController.getAllFacultades);
router.get('/byUniversity', authMiddleware, facultadController.getFacultadesByUniversity);
router.get('/:id', authMiddleware, facultadController.getFacultadById);
router.put('/:id', authMiddleware, facultadController.updateFacultad);
router.delete('/:id', authMiddleware, facultadController.deleteFacultad);

module.exports = router;
